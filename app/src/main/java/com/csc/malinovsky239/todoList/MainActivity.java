package com.csc.malinovsky239.todoList;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.csc.malinovsky239.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    final String order = FeedsTable.COLUMN_IS_DONE + ", " + FeedsTable.COLUMN_DATETIME;
    TodoListCursorAdapter todoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Cursor cursor = getContentResolver().query(Uri.withAppendedPath(ReaderContentProvider.CONTENT_URI, "entries"), new String[]{}, null, new String[]{}, order);
        ListView taskList = (ListView) findViewById(R.id.taskList);
        todoListAdapter = new TodoListCursorAdapter(this, cursor);
        taskList.setAdapter(todoListAdapter);

        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) view.findViewById(R.id.task);
                showChangeTaskDialog(String.valueOf(text.getText()), (text.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) != 0,
                        (int) view.getTag());
            }
        });

        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(FeedsTable.COLUMN_NAME, String.valueOf(((EditText) findViewById(R.id.taskname)).getText()));
                SimpleDateFormat format = new SimpleDateFormat(getString(R.string.datetimeFormat), Locale.getDefault());
                values.put(FeedsTable.COLUMN_DATETIME, format.format(new Date()));
                values.put(FeedsTable.COLUMN_IS_DONE, getString(R.string.FALSE));
                getContentResolver().insert(Uri.withAppendedPath(ReaderContentProvider.CONTENT_URI, "entries"), values);
                Cursor cursor = getContentResolver().query(Uri.withAppendedPath(ReaderContentProvider.CONTENT_URI, "entries"), new String[]{}, null, new String[]{}, order);
                todoListAdapter.changeCursor(cursor);
            }
        });
    }

    public void showChangeTaskDialog(String taskname, boolean isDone, final int id) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.change_task_dialog, null);
        dialogBuilder.setView(dialogView);
        EditText edit_taskname = (EditText) dialogView.findViewById(R.id.edit_taskname);
        edit_taskname.setText(taskname);
        CheckBox is_done = (CheckBox) dialogView.findViewById(R.id.is_done);
        is_done.setChecked(isDone);

        dialogBuilder.setTitle(R.string.ChangeTaskButton);

        dialogBuilder.setPositiveButton(R.string.DoneButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ContentValues values = new ContentValues();
                int is_done = ((CheckBox) dialogView.findViewById(R.id.is_done)).isChecked() ? R.string.TRUE : R.string.FALSE;
                values.put(FeedsTable.COLUMN_IS_DONE, getString(is_done));
                String name = String.valueOf(((EditText) dialogView.findViewById(R.id.edit_taskname)).getText());
                values.put(FeedsTable.COLUMN_NAME, name);
                getContentResolver().update(Uri.withAppendedPath(ReaderContentProvider.CONTENT_URI, "entries"), values, "_id = " + id, null);
                Cursor cursor = getContentResolver().query(Uri.withAppendedPath(ReaderContentProvider.CONTENT_URI, "entries"), new String[]{}, null, new String[]{}, order);
                todoListAdapter.changeCursor(cursor);
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

}
