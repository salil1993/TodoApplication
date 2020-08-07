package com.deftskill.todoapplication.db;

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "deftskill";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";
        public static final String User ="user";
        public static final String COL_TASK_ID ="id";
        public static final String COL_TASK_USER_ID ="user_id";
        public static final String COL_TASK_NAME = "name";
        public static final String COL_TASK_EMAIL = "email";
        public static final String COL_TASK_PASSWORD = "password";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_DATE = "date";
        public static final String COL_TASK_STATUS = "status";
    }
}
