package com.example.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter taskAdapter;

    private List<Task> tasks;

    private static final int ADD_TASK_REQUEST_CODE = 1;

    private void initData() {
        ArrayList<Integer> attachments1 = new ArrayList<>();
        attachments1.add(R.drawable.rails);
        tasks.add(new Task(1, "Buy groceries", "Buy milk, eggs, and bread", "2023-04-22 10:00", "2023-04-25 12:00", false, true, "Shopping", attachments1));

        ArrayList<Integer> attachments2 = new ArrayList<>();
        attachments2.add(R.drawable.tree);
        tasks.add(new Task(2, "Finish project", "Finish the project by the end of the week", "2023-04-22 11:00", "2023-04-29 18:00", false, true, "Work", attachments2));
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
                // Handle task click, e.g., for editing or deleting
                // You can also pass the information to another activity
                // Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
                // intent.putExtra("task", task);
                // startActivity(intent);
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
        taskAdapter.notifyDataSetChanged();
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


}