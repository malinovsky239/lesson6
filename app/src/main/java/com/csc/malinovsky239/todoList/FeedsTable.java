package com.csc.malinovsky239.todoList;

import android.provider.BaseColumns;

interface FeedsTable extends BaseColumns {
    String TABLE_NAME = "tasks";
    String COLUMN_NAME = "name";
    String COLUMN_IS_DONE = "is_done";
    String COLUMN_DATETIME = "datetime";
    String COLUMN_ID = "_id";
}
