package com.firmfreez.android.endpointtest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.firmfreez.android.endpointtest.db.DbSchema.LogTable.Cols;

/**
 * Хелпер для работы с базой лога
 */
public class LogBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = DbSchema.LogTable.Name;
    private static final int VERSION = 1;

    public LogBaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    /**
     * Создание базы
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DB_NAME + "(" +
                "_id integer primary key autoincrement, " +
                Cols.id + ", " +
                Cols.time + ", " +
                Cols.info + ")");
    }

    /**
     * Обновление базы
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
