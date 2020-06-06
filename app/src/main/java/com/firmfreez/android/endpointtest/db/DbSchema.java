package com.firmfreez.android.endpointtest.db;

/**
 * Класс описания базы
 */
public class DbSchema {
    /**
     * Таблица
     */
    public static final class DataTable {
        public static final String Name = "Connections";
        /**
         * Колонки
         */
        public static final class Cols {
            public static final String id = "uuid";
            public static final String Name = "name";
            public static final String isActive = "Active";
            public static final String Good = "good";
            public static final String Bad = "bad";
            public static final String Warning = "warning";
            public static final String RequestType = "request_type";
            public static final String Server = "server";
            public static final String Port = "port";
            public static final String Endpoint = "endpoint";
            public static final String ServerTimeout = "server_timeout";
            public static final String ConnectionTimeout = "connection_timeout";
            public static final String StatusOK = "status_ok";
            public static final String StatusBAD = "status_bad";
            public static final String StatusWarning = "status_warning";
            public static final String ColorOK = "color_ok";
            public static final String ColorBAD = "color_bad";
            public static final String ColorWarning = "color_warning";
        }
    }

    /**
     * Описание таблицы для лога
     */
    public static final class LogTable {
        public static final String Name = "Log";
        public static final class Cols {
            public static final String id = "uuid";
            public static final String time = "time";
            public static final String info = "info";
        }
    }
}
