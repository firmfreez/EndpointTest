package com.firmfreez.android.endpointtest.model;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Модель данных лога
 */
public class LogItemData {
    private UUID mUUID;         //ID для связи с подключением
    private Date mTime;         //Время создания объекта
    private StatusLog mStatus;  //Статус объекта

    public static enum StatusLog {
        GOOD,
        WARNING,
        BAD
    }

    public LogItemData() {
        mUUID = UUID.randomUUID();
        mTime = Calendar.getInstance().getTime();
        mStatus = StatusLog.BAD;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date time) {
        mTime = time;
    }

    public StatusLog getStatus() {
        return mStatus;
    }

    public void setStatus(StatusLog status) {
        mStatus = status;
    }
}
