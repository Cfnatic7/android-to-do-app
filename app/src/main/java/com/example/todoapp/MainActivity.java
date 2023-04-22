package com.example.todoapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter taskAdapter;

    private List<Task> tasks;

    private static final int ADD_TASK_REQUEST_CODE = 1;

    private List<Task> initData() {
        List<Task> tasks = new ArrayList<>();
        ArrayList<Integer> attachments1 = new ArrayList<>();
        attachments1.add(R.drawable.rails);
        tasks.add(new Task(1, "Buy groceries", "Buy milk, eggs, and bread", "2023-04-22 10:00", "2023-04-25 12:00", false, true, "Shopping", attachments1));

        ArrayList<Integer> attachments2 = new ArrayList<>();
        attachments2.add(R.drawable.tree);
        tasks.add(new Task(2, "Finish project", "Finish the project by the end of the week", "2023-04-22 11:00", "2023-04-29 18:00", false, true, "Work", attachments2));

        tasks.add(new Task(3, "Pay bills", "Pay the electricity and water bills", "2023-04-22 12:00", "2023-04-30 23:59", false, false, "Finance", null));
        return tasks;
    }


    private void initRecyclerView() {
        taskAdapter = new TaskAdapter(initData(), new TaskAdapter.OnTaskClickListener() {
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

        initRecyclerView();
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
                // Add the new task to the list and update the RecyclerView
                tasks.add(newTask);
                taskAdapter.notifyDataSetChanged();
            }
        }
    }

}