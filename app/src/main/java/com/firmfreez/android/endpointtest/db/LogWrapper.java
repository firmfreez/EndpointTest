package com.firmfreez.android.endpointtest.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import com.firmfreez.android.endpointtest.db.DbSchema.LogTable.Cols;
import com.firmfreez.android.endpointtest.model.LogItemData;

/**
 * CursorWrapper для удобного преобразования полученной таблицы в объекты
 */
public class LogWrapper extends CursorWrapper {
    public LogWrapper(Cursor cursor) {
        super(cursor);
    }

    /**
     * Возвращает LogItemData из запроса
     * @return
     */
    public LogItemData getData() {
        UUID uuid = UUID.fromString(getString(getColumnIndex(Cols.id)));
        Date time = new Date();
        time.setTime(getLong(getColumnIndex(Cols.time)));
        LogItemData.StatusLog info = LogItemData.StatusLog.valueOf(getString(getColumnIndex(Cols.info)));

        LogItemData logItemData = new LogItemData();
        logItemData.setUUID(uuid);
        logItemData.setTime(time);
        logItemData.setStatus(info);

        return logItemData;
    }
}
