package com.example.todoapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AddTaskActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    protected EditText taskTitleEditText;
    protected EditText taskDescriptionEditText;

    protected DatePicker taskDueDate;

    protected TimePicker taskDueTime;

    protected CheckBox taskStatusCheckBox;
    protected CheckBox taskNotificationCheckBox;
    protected EditText taskCategoryEditText;
    protected Button addAttachmentButton;
    protected Button saveTaskButton;

    protected Long savedTaskId;



    protected static final int REQUEST_CODE_PICK_IMAGE = 1;


    protected ArrayList<String> attachments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        taskTitleEditText = findViewById(R.id.task_title);
        taskDescriptionEditText = findViewById(R.id.task_description);
        taskDueDate = findViewById(R.id.task_due_date);
        taskDueTime = findViewById(R.id.task_due_time);
        taskStatusCheckBox = findViewById(R.id.task_status);
        taskNotificationCheckBox = findViewById(R.id.task_notification);
        taskCategoryEditText = findViewById(R.id.task_category);
        addAttachmentButton = findViewById(R.id.add_attachment_button);
        saveTaskButton = findViewById(R.id.save_task_button);

        attachments = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        initButtons();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("notification_time")) {
             for (Task task : MainActivity.tasks) {
                 updateNotification(task);
             }
        }
    }

    void cancelNotification(Task task) {
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int)task.getId(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private void updateNotification(Task task) {
        cancelNotification(task);

        scheduleNotification(task);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void initButtons() {
        addAttachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickImageIntent, REQUEST_CODE_PICK_IMAGE);
            }
        });


        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask(null);
            }
        });

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void saveTask(Long taskId) {
        String title = taskTitleEditText.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show();
            return;
        }
        String description = taskDescriptionEditText.getText().toString().trim();
        int day = taskDueDate.getDayOfMonth();
        int month = taskDueDate.getMonth() + 1;
        int year = taskDueDate.getYear();

        String dueDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month, day);

        taskDueTime = findViewById(R.id.task_due_time);
        int hour = taskDueTime.getHour();
        int minute = taskDueTime.getMinute();

        String dueTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

        String dueDateTime = dueDate + " " + dueTime;
        boolean status = taskStatusCheckBox.isChecked();
        boolean notification = taskNotificationCheckBox.isChecked();
        String category = taskCategoryEditText.getText().toString().trim();

        if (category.isEmpty()) {
            Toast.makeText(this, "Please enter category", Toast.LENGTH_SHORT).show();
            return;
        }
        String creationTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        Task newTask = new Task(null, title, description, creationTime, dueDateTime, status, notification, category, attachments);

        if (taskId != null) {
            savedTaskId = taskId;
            newTask.setId(taskId);
            MainActivity.taskDbHelper.updateTask(newTask);
        }
        else savedTaskId = MainActivity.taskDbHelper.addTask(newTask);
        newTask.setId(savedTaskId);
        Intent resultIntent = new Intent();
        resultIntent.putExtra("task", newTask);
        setResult(RESULT_OK, resultIntent);
        scheduleNotification(MainActivity.taskDbHelper.getTask(savedTaskId));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            String attachmentPath = copyImageToExternalStorage(selectedImageUri, "attachment_" + System.currentTimeMillis() + ".jpg");

            if (attachmentPath != null) {
                attachments.add(attachmentPath);
                Log.d("AddTaskActivity", "Saved image to external storage: " + attachmentPath);
            } else {
                Log.e("AddTaskActivity", "Failed to save image to external storage");
            }
        }
    }

    private String copyImageToExternalStorage(Uri imageUri, String fileName) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            File outputDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File outputFile = new File(outputDir, fileName);
            OutputStream outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + fileName;
    }

    private void scheduleNotification(Task task) {
        if (!task.isNotificationEnabled()) {
            return;
        }

        Gson gson = new Gson();
        String taskJson = gson.toJson(task);

        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        notificationIntent.putExtra("task", taskJson);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int)task.getId(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        String dueDateString = task.getDueDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date dueDate = null;
        try {
            dueDate = dateFormat.parse(dueDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (dueDate != null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            int notificationMinutesBefore = Integer.parseInt(sharedPreferences.getString("notification_time", "15"));
            long triggerTime = dueDate.getTime() - ((long) notificationMinutesBefore * 60 * 1000);

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }
    }

}
