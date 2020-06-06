package com.firmfreez.android.endpointtest.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.firmfreez.android.endpointtest.db.DataBaseHelper;
import com.firmfreez.android.endpointtest.db.DataWrapper;
import com.firmfreez.android.endpointtest.db.DbSchema;
import com.firmfreez.android.endpointtest.db.DbSchema.DataTable.Cols;
import com.firmfreez.android.endpointtest.db.LogBaseHelper;
import com.firmfreez.android.endpointtest.db.LogWrapper;
import com.firmfreez.android.endpointtest.service.ThreadManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * SINGLETON класс для хранения данных
 */
public class ConnectionPull {
    private static ConnectionPull sInstance;
    private Context mContext;
    private SQLiteDatabase mConnectionDatabase; //База данных подключений
    private SQLiteDatabase mLogDatabase;        //База данных лога

    public static ConnectionPull get(Context context) {
        if (sInstance == null) {
            sInstance = new ConnectionPull(context);
        }
        return sInstance;
    }

    private ConnectionPull(Context context) {
        mContext = context.getApplicationContext();
        mConnectionDatabase = new DataBaseHelper(mContext).getWritableDatabase();
        List<ConnectionItemData> list = getData();
        mLogDatabase = new LogBaseHelper(mContext).getWritableDatabase();
    }

    /**
     * Собирает ContentValues лога из объекта data
     * @param data
     * @return ContentValuse
     */
    private static ContentValues getLogContentValues(LogItemData data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbSchema.LogTable.Cols.id, data.getUUID().toString());
        contentValues.put(DbSchema.LogTable.Cols.time,String.valueOf(data.getTime().getTime()));
        contentValues.put(DbSchema.LogTable.Cols.info,data.getStatus().toString());

        return contentValues;
    }

    /**
     * Удаляет соединение и вместе с ним очищает лог и уничтожает поток
     * @param uuid
     */
    public void removeConnection(UUID uuid) {
        if(ThreadManager.getInstance(mContext).hasThread(uuid)) {
            ThreadManager.getInstance(mContext).stopThread(uuid);
        }
        mConnectionDatabase.delete(DbSchema.DataTable.Name,
                Cols.id + " = ?", new String[]{uuid.toString()});
        clearLog(uuid);
    }

    /**
     * Очищает лог по UUID
     * @param uuid
     */
    public void clearLog(UUID uuid) {
        mLogDatabase.delete(DbSchema.LogTable.Name,null,null);
    }

    /**
     * Добавляет лог в базу
     * @param data
     */
    public void addLog(LogItemData data) {
        mLogDatabase.insert(DbSchema.LogTable.Name,null,getLogContentValues(data));
    }

    /**
     * Возвращает список записей лога конкретного подключения по UUID
     * @param uuid UUID аккаунта
     * @return
     */
    public List<LogItemData> getLogList(UUID uuid) {
        List<LogItemData> data = new ArrayList<>();
        LogWrapper cursor = queryLog(DbSchema.LogTable.Cols.id + " = ?",new String[] {uuid.toString()});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                data.add(cursor.getData());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return data;
    }

    /**
     * Возвращает список лога из базы
     * @param WhereClause WHERE 1 = ?
     * @param WhereArgs ? из WHERE
     * @return LogWrapper
     */
    public LogWrapper queryLog(String WhereClause, String[] WhereArgs) {
        Cursor cursor = mLogDatabase.query(DbSchema.LogTable.Name,
                null, WhereClause, WhereArgs,
                null,
                null,
                null);
        return new LogWrapper(cursor);
    }

    /**
     * Преобразует объект подключения в ContentValue
     * @param data - Объект подключения
     * @return ContentValues - необходимый для запроса
     */
    private static ContentValues getConnectionContentValues(ConnectionItemData data) {
        ContentValues values = new ContentValues();
        values.put(Cols.id, data.getId().toString());
        values.put(Cols.Name, data.getName());
        values.put(Cols.isActive, data.isActive());
        values.put(Cols.Good,data.getGood());
        values.put(Cols.Bad,data.getBad());
        values.put(Cols.Warning,data.getWarning());
        values.put(Cols.RequestType, data.getType().toString());
        values.put(Cols.Server, data.getServer());
        values.put(Cols.Port, data.getPort());
        values.put(Cols.Endpoint, data.getEndpoint());
        values.put(Cols.ServerTimeout, data.getServerTimeOut());
        values.put(Cols.ConnectionTimeout, data.getConnectionTimeOut());
        values.put(Cols.StatusOK, data.getStatusOK());
        values.put(Cols.StatusBAD, data.getStatusBAD());
        values.put(Cols.StatusWarning, data.getStatusWarning());
        values.put(Cols.ColorOK, data.getStatusOKColor());
        values.put(Cols.ColorBAD, data.getStatusBADColor());
        values.put(Cols.ColorWarning,data.getStatusWarningColor());
        return values;
    }

    /**
     * Делает запрос к базе и возвращает список всех подключений.
     * @return список всех подключений
     */
    public List<ConnectionItemData> getData() {
        //
        List<ConnectionItemData> data = new ArrayList<>();
        try (DataWrapper cursor = queryConnectionListData(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                data.add(cursor.getData());
                cursor.moveToNext();
            }
        }
        return data;
    }

    /**
     * Добавляет новое подключение в базу.
     * @param connection Объект подключения
     */
    public void addConnection(ConnectionItemData connection) {
        ContentValues contentValues = getConnectionContentValues(connection);
        mConnectionDatabase.insert(DbSchema.DataTable.Name,null,contentValues);
    }

    /**
     * Обновляет объект подключения в базе по UUID.
     * @param connection Новый объект подключения.
     */
    public void updateConnection(ConnectionItemData connection) {
        ContentValues contentValues = getConnectionContentValues(connection);
        mConnectionDatabase.update(DbSchema.DataTable.Name, contentValues, Cols.id + " = ?",
                new String[]{connection.getId().toString()});
    }

    /**
     * Увеличивает счетчик успешных запросов подключния на 1
     * @param id
     */
    public void incGood(UUID id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Cols.Good, getConnection(id).getGood() + 1);
        mConnectionDatabase.update(DbSchema.DataTable.Name,contentValues,
                Cols.id + " = ?",new String[]{id.toString()});
    }

    /**
     * Увеличивает счетчик предупреждающих запросов подключения на 1
     * @param id
     */
    public void incWarning(UUID id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Cols.Warning, getConnection(id).getWarning() + 1);
        mConnectionDatabase.update(DbSchema.DataTable.Name,contentValues,
                Cols.id + " = ?",new String[]{id.toString()});
    }

    /**
     * Увеличивает счетчик неудачных запросов подключения на 1
     * @param id
     */
    public void incBad(UUID id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Cols.Bad, getConnection(id).getBad() + 1);
        mConnectionDatabase.update(DbSchema.DataTable.Name,contentValues,
                Cols.id + " = ?",new String[]{id.toString()});
    }


    /**
     * Возвращает ConnectionItemData из базы
     * @param id - UUID подключения
     * @return ConnectionItemData
     */
    public ConnectionItemData getConnection(UUID id) {
        DataWrapper wrapper = queryConnectionListData(Cols.id + " = ?",
                new String[]{id.toString()});
        if(wrapper.getCount() == 0) {
            return null;
        }
        ConnectionItemData data;
        try {
            wrapper.moveToFirst();
            data = wrapper.getData();
        } finally {
            wrapper.close();
        }
        return data;
    }

    /**
     * Обновляет информацию о подключении в базе
     * если подключение в базе отсутствует - то добавляет его.
     * @param connectionItemData подключение.
     */
    public void addOrUpdate(ConnectionItemData connectionItemData) {
        if (getConnection(connectionItemData.getId()) == null) {
            addConnection(connectionItemData);
        } else {
            updateConnection(connectionItemData);
        }
    }

    /**
     * Делает запрос к базе и преобразует полученный Cursor в кастомный CursorWrapper
     * @param whereClause - WHERE [whereClause] - параметры отбора.
     * @param whereArgs - Переменные, используемые в whereClause (во избежание SQL инъекций)
     * @return кастомный CursorWrapper (DataWrapper)
     */
    private DataWrapper queryConnectionListData(String whereClause, String[] whereArgs) {
        Cursor cursor =  mConnectionDatabase.query(DbSchema.DataTable.Name,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);
        return new DataWrapper(cursor);
    }
}
