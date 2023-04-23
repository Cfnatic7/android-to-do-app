package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter taskAdapter;

    public static List<Task> tasks;

    private static final int ADD_TASK_REQUEST_CODE = 1;

    private void initData() {
        String railsImagePath = copyImageToExternalStorage(R.drawable.rails, "rails.jpg");
        ArrayList<String> attachments1 = new ArrayList<>();
        attachments1.add(railsImagePath);
        tasks.add(new Task(1, "Buy groceries", "Buy milk, eggs, and bread", "2023-04-22 10:00", "2023-04-25 12:00", false, true, "Shopping", attachments1));

        String treeImagePath = copyImageToExternalStorage(R.drawable.tree, "tree.jpg");
        ArrayList<String> attachments2 = new ArrayList<>();
        attachments2.add(treeImagePath);
        tasks.add(new Task(2, "Finish project", "Finish the project by the end of the week", "2023-04-22 11:00", "2023-04-29 18:00", false, true, "Work", attachments2));
    }


    private String copyImageToExternalStorage(int resourceId, String fileName) {
        try {
            InputStream inputStream = getResources().openRawResource(resourceId);
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



    private void initRecyclerView() {

        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        boolean hideCompletedTasks = sharedPreferences.getBoolean("hide_completed_tasks", false);

        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (!hideCompletedTasks || !task.isCompleted()) {
                filteredTasks.add(task);
            }
        }

        taskAdapter = new TaskAdapter(filteredTasks, new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onTaskClick(Task task) {
                Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
                intent.putExtra("task", task);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tasks = new ArrayList<>();
        initData();

        initRecyclerView();
        initFab();
        initSettingsButton();

    }

    private void initFab() {
        FloatingActionButton fab = findViewById(R.id.fab_add_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle FAB click, e.g., open a dialog or activity to create a new task
                 Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                 startActivityForResult(intent, ADD_TASK_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Task newTask = (Task) data.getSerializableExtra("task");
            if (newTask != null) {
                addTaskToList(newTask);
            }
        }
    }

    private void addTaskToList(Task newTask) {
        tasks.add(newTask);
        taskAdapter.updateTasks(tasks);
    }

    private void initSettingsButton() {
        Button settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter_category) {
            showCategoryFilterDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCategoryFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("choose category");

        // Pobierz listę unikalnych kategorii z listy zadań
        Set<String> uniqueCategories = new HashSet<>();
        for (Task task : tasks) {
            uniqueCategories.add(task.getCategory());
        }
        List<String> categoriesList = new ArrayList<>(uniqueCategories);
        categoriesList.add(0, "all categories");
        String[] categories = categoriesList.toArray(new String[0]);

        builder.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // Jeśli wybrano "Wszystkie", wyświetl wszystkie zadania
                    taskAdapter.updateTasks(tasks);
                } else {
                    // Filtruj zadania według wybranej kategorii
                    String selectedCategory = categories[which];
                    filterTasksByCategory(selectedCategory);
                }
            }
        });

        builder.setNegativeButton("cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void filterTasksByCategory(String category) {
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getCategory().equals(category)) {
                filteredTasks.add(task);
            }
        }

        taskAdapter.updateTasks(filteredTasks);
    }

    private void cancelNotification(Task task) {
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, task.getId(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }


}