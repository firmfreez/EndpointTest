package com.firmfreez.android.endpointtest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.firmfreez.android.endpointtest.db.DbSchema.*;

/**
 * Необходим для работы с базой
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final int Version = 1;
    private static final String DB_Name = DataTable.Name;

    public DataBaseHelper(@Nullable Context context) {
        super(context, DB_Name, null, Version);
    }

    /**
     * Действия при создании бащы
     * @param db эехемпляр базы
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DB_Name + "(" +
                 "_id integer primary key autoincrement, " +
                DataTable.Cols.id + ", " +
                DataTable.Cols.Name + ", " +
                DataTable.Cols.isActive + ", " +
                DataTable.Cols.Good + ", " +
                DataTable.Cols.Bad + ", " +
                DataTable.Cols.Warning + ", " +
                DataTable.Cols.RequestType + ", " +
                DataTable.Cols.Server + ", " +
                DataTable.Cols.Port + ", " +
                DataTable.Cols.Endpoint + ", " +
                DataTable.Cols.ServerTimeout + ", " +
                DataTable.Cols.ConnectionTimeout + ", " +
                DataTable.Cols.StatusOK + ", " +
                DataTable.Cols.StatusBAD + ", " +
                DataTable.Cols.StatusWarning + ", " +
                DataTable.Cols.ColorOK + ", " +
                DataTable.Cols.ColorBAD + ", " +
                DataTable.Cols.ColorWarning + ")");
    }

    /**
     * Действия при обновлении базы
     * @param db экземпляр базы
     * @param oldVersion старая версия
     * @param newVersion новая версия
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
