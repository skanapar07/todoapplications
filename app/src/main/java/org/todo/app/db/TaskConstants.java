
package org.todo.app.db;

import android.provider.BaseColumns;

public final class TaskConstants {

    private TaskConstants() {}

    public static final class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TASK_NAME = "task_name";
    }
}
