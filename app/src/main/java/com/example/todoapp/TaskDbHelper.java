package com.example.todoapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TaskDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";

    public static final String CREATION_TIME = "creation_time";

    public static final String DUE_DATE = "due_date";

    public static final String completed = "completed";

    public static final String notificationEnabled = "notification_enabled";

    private static final String category = "category";

    private static final String attachments = "attachments";

    private Gson gson = new Gson();

    private static final String CREATE_TABLE_TASKS = "CREATE TABLE " + TABLE_TASKS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_TITLE + " TEXT NOT NULL, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + CREATION_TIME + " TEXT NOT NULL, "
            + DUE_DATE + " TEXT, "
            + completed + " INTEGER DEFAULT 0, " // Użyj INTEGER dla wartości boolean (0 - false, 1 - true)
            + notificationEnabled + " INTEGER DEFAULT 0, " // Użyj INTEGER dla wartości boolean (0 - false, 1 - true)
            + category + " TEXT, "
            + attachments + " TEXT" // Zapisywanie listy załączników jako tekstu (JSON)
            + ")";

    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }


    @SuppressLint("Range")
    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Gson gson = new Gson();

        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(CREATION_TIME, task.getCreationTime());
        values.put(DUE_DATE, task.getDueDate());
        values.put(completed, task.isCompleted() ? 1 : 0); // Konwersja boolean na wartość INTEGER
        values.put(notificationEnabled, task.isNotificationEnabled() ? 1 : 0); // Konwersja boolean na wartość INTEGER
        values.put(category, task.getCategory());

        // Konwersja listy załączników na JSON
        List<String> attachments = task.getAttachments();
        String attachmentsJson = gson.toJson(attachments);
        values.put(TaskDbHelper.attachments, attachmentsJson);

        long id = db.insert(TABLE_TASKS, null, values);
        db.close();
        return id;
    }


    // Metoda aktualizowania zadania w bazie danych
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Gson gson = new Gson();

        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(CREATION_TIME, task.getCreationTime());
        values.put(DUE_DATE, task.getDueDate());
        values.put(completed, task.isCompleted() ? 1 : 0);
        values.put(notificationEnabled, task.isNotificationEnabled() ? 1 : 0);
        values.put(category, task.getCategory());

        List<String> attachments = task.getAttachments();
        String attachmentsJson = gson.toJson(attachments);
        values.put(TaskDbHelper.attachments, attachmentsJson);

        int updatedRows = db.update(TABLE_TASKS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
        return updatedRows;
    }

    @SuppressLint("Range")
    public Task getTask(Long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Task task = null;

        // Używamy zapytania z klauzulą WHERE, aby znaleźć zadanie o określonym identyfikatorze
        String selectQuery = "SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_ID + " = " + id;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            Gson gson = new Gson();
            task = new Task();
            task.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
            task.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            task.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
            task.setCreationTime(cursor.getString(cursor.getColumnIndex(CREATION_TIME)));
            task.setDueDate(cursor.getString(cursor.getColumnIndex(DUE_DATE)));
            task.setCompleted(cursor.getInt(cursor.getColumnIndex(completed)) == 1); // Konwersja INTEGER na wartość boolean
            task.setNotificationEnabled(cursor.getInt(cursor.getColumnIndex(notificationEnabled)) == 1); // Konwersja INTEGER na wartość boolean
            task.setCategory(cursor.getString(cursor.getColumnIndex(category)));

            // Konwersja JSON na listę załączników
            String attachmentsJson = cursor.getString(cursor.getColumnIndex(attachments));
            Type attachmentListType = new TypeToken<List<String>>() {}.getType();
            List<String> attachments = gson.fromJson(attachmentsJson, attachmentListType);
            task.setAttachments(attachments);
        }

        cursor.close();
        db.close();

        return task;
    }


    public int updateTask(Long taskId) {
        Task task = getTask(taskId);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Gson gson = new Gson();

        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(CREATION_TIME, task.getCreationTime());
        values.put(DUE_DATE, task.getDueDate());
        values.put(completed, task.isCompleted() ? 1 : 0);
        values.put(notificationEnabled, task.isNotificationEnabled() ? 1 : 0);
        values.put(category, task.getCategory());

        List<String> attachments = task.getAttachments();
        String attachmentsJson = gson.toJson(attachments);
        values.put(TaskDbHelper.attachments, attachmentsJson);

        int updatedRows = db.update(TABLE_TASKS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
        return updatedRows;
    }


    // Metoda usuwania zadania z bazy danych
    public void deleteTask(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<String> getAttachments(long taskId) {
        List<String> attachments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS, new String[]{TaskDbHelper.attachments},
                COLUMN_ID + "=?", new String[]{String.valueOf(taskId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String attachmentsJson = cursor.getString(cursor.getColumnIndex(TaskDbHelper.attachments));
            Type listType = new TypeToken<ArrayList<String>>(){}.getType();
            attachments = new Gson().fromJson(attachmentsJson, listType);
            cursor.close();
        }
        db.close();
        return attachments;
    }


    // Metoda pobierająca listę wszystkich zadań z bazy danych
    @SuppressLint("Range")
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();

        // Zapytanie do bazy danych
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Przechodzenie przez wszystkie wiersze i dodawanie ich do listy
        if (cursor.moveToFirst()) {
            Gson gson = new Gson();
            do {
                Task task = new Task();
                task.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                task.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                task.setCreationTime(cursor.getString(cursor.getColumnIndex(CREATION_TIME)));
                task.setDueDate(cursor.getString(cursor.getColumnIndex(DUE_DATE)));
                task.setCompleted(cursor.getInt(cursor.getColumnIndex(completed)) == 1);
                task.setNotificationEnabled(cursor.getInt(cursor.getColumnIndex(notificationEnabled)) == 1);
                task.setCategory(cursor.getString(cursor.getColumnIndex(category)));

                String attachmentsJson = cursor.getString(cursor.getColumnIndex(attachments));
                Type attachmentListType = new TypeToken<List<String>>() {}.getType();
                List<String> attachments = gson.fromJson(attachmentsJson, attachmentListType);
                task.setAttachments(attachments);

                tasks.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return tasks;
    }

}
