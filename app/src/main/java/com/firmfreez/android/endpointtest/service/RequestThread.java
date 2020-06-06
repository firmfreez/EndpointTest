package com.firmfreez.android.endpointtest.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.firmfreez.android.endpointtest.R;
import com.firmfreez.android.endpointtest.model.ConnectionItemData;
import com.firmfreez.android.endpointtest.model.ConnectionPull;
import com.firmfreez.android.endpointtest.model.LogItemData;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;

/**
 * Экземпляр потока
 */
public class RequestThread implements Runnable {
    private Handler mHandler;
    private Context mContext;
    private UUID mUUID;
    private ThreadManager.ThreadState mState;
    private onRequestSendedListener mOnRequestSendedListener;
    private int mBadCounter = 0;

    /**
     * Слушатель для взаимодействия с GUI
     * @param onRequestSendedListener Listener
     */
    public void setOnRequestSendedListener(onRequestSendedListener onRequestSendedListener) {
        mOnRequestSendedListener = onRequestSendedListener;
    }

    public RequestThread(Context context, UUID uuid) {
        mContext = context;
        mUUID = uuid;
        mState = ThreadManager.ThreadState.STOP;
    }

    @Override
    public void run() {
        //Делаем фоновую работу тут
        while (mState != ThreadManager.ThreadState.STOP) {
            Log.i("THREAD", mUUID.toString());

            try {
                ConnectionItemData connectionItemData = ConnectionPull.get(mContext).getConnection(mUUID);

                //Получение данных с базы
                String server = connectionItemData.getServer();
                int port = connectionItemData.getPort();
                if(port != 0) {
                    server = server + ":" + port;
                }
                String endpoint = connectionItemData.getEndpoint();
                int connection_timeout = connectionItemData.getConnectionTimeOut();
                int server_timeout = connectionItemData.getServerTimeOut();
                ConnectionItemData.RequestType type = connectionItemData.getType();

                //Составляем запрос
                int code = 0;
                if(server.contains("http")) {
                    //Делаем запрос
                    Retrofit retrofit = new Retrofit.Builder().baseUrl(server).build();
                    try {
                        switch (type) {
                            case GET:
                                GetRequest get = retrofit.create(GetRequest.class);
                                code = get.makeRequest(endpoint).execute().code();
                                break;
                            case POST:
                                PostRequest post = retrofit.create(PostRequest.class);
                                code = post.makeRequest(endpoint).execute().code();
                                break;
                            case PUT:
                                PutRequest put = retrofit.create(PutRequest.class);
                                code = put.makeRequest(endpoint).execute().code();
                                break;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //Добавляем лог
                LogItemData logItemData = new LogItemData();
                logItemData.setUUID(mUUID);
                logItemData.setStatus(codeToStatus(code));
                ConnectionPull.get(mContext).addLog(logItemData);

                if(mBadCounter >= server_timeout) {
                    mBadCounter = 0;
                    //Отправляем уведомление
                    NotificationManager notificationManager = (NotificationManager)
                            mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("Предупреждение")
                            .setContentText(String.format("Отправленно %1$d неудачных запросов.",server_timeout));
                    Notification notification = builder.build();
                    notificationManager.notify(1,notification);
                }

                //Обновляем фрагмент
                if (mHandler != null) {
                    //Отпраляем сообщение с результатом в хандлер
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mOnRequestSendedListener.onSended(logItemData);
                        }
                    });
                    Log.i("THREAD", "YES HANDLER");
                } else {
                    Log.i("THREAD", "NO HANDLER");
                }

                Log.i("THREAD", "CODE: " + code);
                synchronized (this) {
                    try {
                        wait(TimeUnit.SECONDS.toMillis(connection_timeout));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            //Пауза
            synchronized (this) {
                try {
                    while (mState == ThreadManager.ThreadState.PAUSE) {
                        wait(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ThreadManager.ThreadState getState() {
        return mState;
    }

    public void setState(ThreadManager.ThreadState state) {
        mState = state;
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    public UUID getUUID() {
        return mUUID;
    }

    private LogItemData.StatusLog codeToStatus(int code) {
        switch (code) {
            case 200:
                ConnectionPull.get(mContext).incGood(mUUID);
                return LogItemData.StatusLog.GOOD;
            case 401:
            case 461:
                ConnectionPull.get(mContext).incWarning(mUUID);
                return LogItemData.StatusLog.WARNING;
            default:
                ConnectionPull.get(mContext).incBad(mUUID);
                mBadCounter++;
                return LogItemData.StatusLog.BAD;
        }
    }
}
