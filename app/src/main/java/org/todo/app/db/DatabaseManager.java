
package org.todo.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "task_database.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + TaskConstants.TaskEntry.TABLE_NAME + " (" +
                TaskConstants.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskConstants.TaskEntry.COLUMN_TASK_NAME + " TEXT NOT NULL);";
        db.execSQL(createTableSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskConstants.TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}
