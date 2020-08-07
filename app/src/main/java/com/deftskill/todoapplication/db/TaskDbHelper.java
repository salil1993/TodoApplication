package com.deftskill.todoapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.deftskill.todoapplication.ui.LoginActivity;
import com.deftskill.todoapplication.Model.Todo;
import com.deftskill.todoapplication.Model.User;

import java.util.ArrayList;
import java.util.List;

public class TaskDbHelper extends SQLiteOpenHelper {
    private static final String Table1 = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
            TaskContract.TaskEntry.COL_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL," +
            TaskContract.TaskEntry.COL_TASK_DATE + " TEXT ," +
            TaskContract.TaskEntry.COL_TASK_STATUS + " TEXT " +
            ");";
    private static final String Table2 = "CREATE TABLE " + TaskContract.TaskEntry.User + " ( " +
            TaskContract.TaskEntry.COL_TASK_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TaskContract.TaskEntry.COL_TASK_NAME + " TEXT NOT NULL," +
            TaskContract.TaskEntry.COL_TASK_EMAIL + " TEXT ," +
            TaskContract.TaskEntry.COL_TASK_PASSWORD + " TEXT " +
            ");";
    private Context context;

    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
   /*     String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL," +
                TaskContract.TaskEntry.COL_TASK_DATE + " TEXT ,"+
                TaskContract.TaskEntry.COL_TASK_STATUS + " TEXT "+
                ");";*/
        db.execSQL(Table1);
        db.execSQL(Table2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.User);
        onCreate(db);
    }

    /*======================insertion===========================================*/
    public void addTodo(Todo Todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TaskContract.TaskEntry.COL_TASK_TITLE, Todo.getTitle());
        cv.put(TaskContract.TaskEntry.COL_TASK_DATE, Todo.getDate());
        cv.put(TaskContract.TaskEntry.COL_TASK_STATUS, Todo.getStatus());
        long result = db.insert(TaskContract.TaskEntry.TABLE, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    /*================updation============================*/
    public void updateTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TaskContract.TaskEntry.COL_TASK_TITLE, todo.getTitle());
        cv.put(TaskContract.TaskEntry.COL_TASK_DATE, todo.getDate());
        cv.put(TaskContract.TaskEntry.COL_TASK_STATUS, todo.getStatus());
        String where = TaskContract.TaskEntry.COL_TASK_TITLE + "=?";
        String[] whereArgs = new String[]{String.valueOf(todo.getTitle())};
        long result = db.update(TaskContract.TaskEntry.TABLE, cv, where, whereArgs);

        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }


    /*insertion User*/
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TaskContract.TaskEntry.COL_TASK_NAME, user.getName());
        cv.put(TaskContract.TaskEntry.COL_TASK_EMAIL, user.getEmail());
        cv.put(TaskContract.TaskEntry.COL_TASK_PASSWORD, user.getPassword());
        long result = db.insert(TaskContract.TaskEntry.User, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Registered Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);

        }
    }

    public boolean checkUserExist(String username, String password) {
        String[] columns = {"email"};
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = "email=? and password = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TaskContract.TaskEntry.User, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        close();
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }


    public List<Todo> getAllTodo() {
        List<Todo> todo = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TaskContract.TaskEntry.TABLE + " ORDER BY " +
                TaskContract.TaskEntry.COL_TASK_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Todo note = new Todo();
                note.setTitle(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE)));
                note.setDate(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_DATE)));
                note.setStatus(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_STATUS)));
                todo.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return todo list
        return todo;
    }


}
