
package org.todo.app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;

import org.todo.app.db.DatabaseManager;
import org.todo.app.db.TaskConstants;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    private DatabaseManager databaseManager;
    private ArrayAdapter<String> taskAdapter;
    private ListView taskListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseManager = new DatabaseManager(this);
        taskListView = findViewById(R.id.task_list);
        loadTasks();
    }

    private void loadTasks() {
        ArrayList<String> tasks = new ArrayList<>();
        SQLiteDatabase db = databaseManager.getReadableDatabase();
        Cursor cursor = db.query(TaskConstants.TaskEntry.TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String taskName = cursor.getString(cursor.getColumnIndex(TaskConstants.TaskEntry.COLUMN_TASK_NAME));
            tasks.add(taskName);
        }

        if (taskAdapter == null) {
            taskAdapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.task_name, tasks);
            taskListView.setAdapter(taskAdapter);
        } else {
            taskAdapter.clear();
            taskAdapter.addAll(tasks);
            taskAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add_task) {
            showTaskDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showTaskDialog() {
        final EditText taskInput = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Add Task")
                .setMessage("Enter a new task:")
                .setView(taskInput)
                .setPositiveButton("Add", (dialog, which) -> {
                    String taskName = taskInput.getText().toString();
                    saveTask(taskName);
                    loadTasks();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void saveTask(String taskName) {
        SQLiteDatabase db = databaseManager.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskConstants.TaskEntry.COLUMN_TASK_NAME, taskName);
        db.insert(TaskConstants.TaskEntry.TABLE_NAME, null, values);
        db.close();
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        EditText taskTextView = parent.findViewById(R.id.task_name);
        String taskName = taskTextView.getText().toString();

        SQLiteDatabase db = databaseManager.getWritableDatabase();
        db.delete(TaskConstants.TaskEntry.TABLE_NAME, TaskConstants.TaskEntry.COLUMN_TASK_NAME + "=?", new String[]{taskName});
        db.close();
        loadTasks();
    }
}
