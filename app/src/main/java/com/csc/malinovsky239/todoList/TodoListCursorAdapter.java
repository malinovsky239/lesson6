package com.csc.malinovsky239.todoList;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.csc.malinovsky239.R;

public class TodoListCursorAdapter extends CursorAdapter {
    public TodoListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvTask = (TextView) view.findViewById(R.id.task);
        TextView tvDatetime = (TextView) view.findViewById(R.id.datetime);
        String task = cursor.getString(cursor.getColumnIndexOrThrow(FeedsTable.COLUMN_NAME));
        String datetime = cursor.getString(cursor.getColumnIndexOrThrow(FeedsTable.COLUMN_DATETIME));
        tvTask.setText(task);
        tvDatetime.setText(datetime);
        String isDone = cursor.getString(cursor.getColumnIndexOrThrow(FeedsTable.COLUMN_IS_DONE));
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(FeedsTable.COLUMN_ID));
        view.setTag(id);
        if (isDone.equals("TRUE")) {
            tvTask.setPaintFlags(tvTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvDatetime.setPaintFlags(tvDatetime.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tvTask.setPaintFlags(tvTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            tvDatetime.setPaintFlags(tvDatetime.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}
