package com.firmfreez.android.endpointtest.service;

import android.content.Context;
import android.os.Handler;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Синглтон - менеджер для управления потоками
 */
public class ThreadManager {
    private static ThreadManager sInstance;
    private List<Thread> mThreads;
    private Context mContext;
    private ConcurrentHashMap<Thread,RequestThread> mThreadMap;
    public enum ThreadState {
        START,
        STOP,
        PAUSE
    }

    public static ThreadManager getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new ThreadManager(context);
        }
        return sInstance;
    }

    private ThreadManager(Context context) {
        mContext = context;
        mThreads = new ArrayList<>();
        mThreadMap = new ConcurrentHashMap<>();
    }

    /**
     * Возвращает поток по ID подключения
     * @param id
     * @return
     */
    @org.jetbrains.annotations.Nullable
    private Thread getThread(UUID id) {
        for(int i = 0; i < mThreads.size(); i++) {
            if(mThreadMap.get(mThreads.get(i)).getUUID().equals(id)) {
                return mThreads.get(i);
            }
        }
        return null;
    }

    /**
     * Возвращает Runnable объект для взаимодействия с потоком по ID покдлючения
     * @param id
     * @return RequestThread
     */
    @Nullable
    private RequestThread getRequestThread(UUID id) {
        for(int i = 0; i < mThreads.size(); i++) {
            if(mThreadMap.get(mThreads.get(i)).getUUID().equals(id)) {
                return mThreadMap.get(mThreads.get(i));
            }
        }
        return null;
    }

    /**
     * Создаёт поток в менеджере для подключения
     * @param id UUID подключения
     */
    public void addThread(UUID id) {
        RequestThread requestThread = new RequestThread(mContext,id);
        Thread thread = new Thread(requestThread);
        mThreadMap.put(thread,requestThread);
        mThreads.add(thread);
    }

    /**
     * Удаляет поток из менеджера
     * @param id
     */
    public void removeThread(UUID id) {
        Thread thr = getThread(id);
        if(thr == null) {
            return;
        }
        detachHandler(id);
        mThreadMap.remove(thr);
        mThreads.remove(thr);
    }

    /**
     * Запускает поток, если он уже запущен - то возобновляет его
     * @param id
     */
    public void startThread(UUID id) {
        Thread thr = getThread(id);
        if(thr == null) {
            return;
        }
        RequestThread requestThread = getRequestThread(id);
        if(requestThread == null) {
            return;
        }
        requestThread.setState(ThreadState.START);
        if(!thr.isAlive()) {
            thr.start();
        }
    }

    /**
     * Устанавливает слушателя и Handler для взаимодействия с GUI фрагмента из потока
     * @param listener Слушатель
     * @param handler Handler
     * @param id UUID подключения
     */
    public void setOnRequestSendedListener(onRequestSendedListener listener, Handler handler, UUID id) {
        RequestThread requestThread = getRequestThread(id);
        if(requestThread == null) {
            return;
        }
        requestThread.setOnRequestSendedListener(listener);
        attachHandler(id,handler);
    }

    /**
     * Останавливает поток
     * @param id UUID подключения
     */
    public void stopThread(UUID id) {
        Thread thr = getThread(id);
        if(thr == null) {
            return;
        }
        RequestThread requestThread = getRequestThread(id);
        if(requestThread == null) {
            return;
        }
        requestThread.setState(ThreadState.STOP);
        thr.interrupt();
        removeThread(id);
    }

    /**
     * Приостанавливает поток
     * @param id UUID подключения
     */
    public void pauseThread(UUID id) {
        RequestThread requestThread = getRequestThread(id);
        if(requestThread == null) {
            return;
        }
        requestThread.setState(ThreadState.PAUSE);
    }

    /**
     * Прикрепляет Handler к потоку
     * @param id UUID подключения
     * @param handler Handler
     */
    private void attachHandler(UUID id, Handler handler) {
        RequestThread requestThread = getRequestThread(id);
        if(requestThread == null) {
            return;
        }
        requestThread.setHandler(handler);
    }

    /**
     * Получение состояния потока START | PAUSE | STOP
     * @param id UUID подключения
     * @return ThreadState
     */
    public ThreadState getThreadState(UUID id) {
        RequestThread requestThread = getRequestThread(id);
        if(requestThread == null) {
            return ThreadState.STOP;
        }
        return requestThread.getState();
    }

    /**
     * Открепляет Handler и Listener от потока
     * @param id UUID подключения
     */
    public void detachHandler(UUID id) {
        RequestThread requestThread = getRequestThread(id);
        if(requestThread == null) {
            return;
        }
        requestThread.setHandler(null);
        requestThread.setOnRequestSendedListener(null);
    }

    /**
     * Возвращает true, если у указанного UUID поток уже создан
     * @param id UUID подключения
     * @return boolean
     */
    public boolean hasThread(UUID id) {
        Thread thr = getThread(id);
        return thr != null;
    }

}
