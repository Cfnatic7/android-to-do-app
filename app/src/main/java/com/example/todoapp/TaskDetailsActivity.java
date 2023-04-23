package com.example.todoapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TaskDetailsActivity extends AppCompatActivity {

    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 101;

    private static final int EDIT_TASK_REQUEST_CODE = 1001;

    private Button editTaskButton;

    private static final int REQUEST_CODE_EDIT_TASK = 2;

    Task task;

    private TextView taskTitle;
    private TextView taskDescription;
    private TextView taskCreationTime;
    private TextView taskDueDate;
    private TextView taskStatus;
    private TextView taskCategory;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        taskTitle = findViewById(R.id.task_title);
        taskDescription = findViewById(R.id.task_description);
        taskCreationTime = findViewById(R.id.task_creation_time);
        taskDueDate = findViewById(R.id.task_due_date);
        taskStatus = findViewById(R.id.task_status);
        taskCategory = findViewById(R.id.task_category);
        recyclerView = findViewById(R.id.attachment_recycler_view);

        task = (Task) getIntent().getSerializableExtra("task");

        System.out.println("Task details on create: " + task.toString());

        taskTitle.setText(task.getTitle());
        taskDescription.setText(task.getDescription());
        taskCreationTime.setText(task.getCreationTime());
        taskDueDate.setText(task.getDueDate());
        taskStatus.setText(task.isCompleted() ? "done" : "pending");
        taskCategory.setText(task.getCategory());

        // Ustawienie RecyclerView dla załączników
        AttachmentAdapter attachmentAdapter = new AttachmentAdapter(new ArrayList<>(task.getAttachments()), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(attachmentAdapter);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
        }
        editTaskButton = findViewById(R.id.edit_task_button);
        initEditTaskButton();
        Button editTaskButton = findViewById(R.id.edit_task_button);
        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editTaskIntent = new Intent(TaskDetailsActivity.this, EditTaskActivity.class);
                editTaskIntent.putExtra("task", task);
                startActivityForResult(editTaskIntent, EDIT_TASK_REQUEST_CODE);
            }
        });

    }


    private void initEditTaskButton() {
        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTask();
            }
        });
    }

    private void editTask() {
        Intent editTaskIntent = new Intent(this, EditTaskActivity.class);
        editTaskIntent.putExtra("task", task);
        startActivityForResult(editTaskIntent, REQUEST_CODE_EDIT_TASK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_TASK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Task updatedTask = (Task) data.getSerializableExtra("task");
            if (updatedTask != null) {
                task = updatedTask;
                updateTaskDetails();
            }
        }
    }

    private void updateTaskDetails() {
        taskTitle.setText(task.getTitle());
        taskDescription.setText(task.getDescription());
        taskCreationTime.setText(task.getCreationTime());
        taskDueDate.setText(task.getDueDate());
        taskStatus.setText(task.isCompleted() ? "done" : "pending");
        taskCategory.setText(task.getCategory());

        // Uaktualnienie załączników
        AttachmentAdapter attachmentAdapter = new AttachmentAdapter(new ArrayList<>(task.getAttachments()), this);
        recyclerView.setAdapter(attachmentAdapter);
        System.out.println(task.toString());
        for (int i = 0; i < MainActivity.tasks.size(); i++) {
            System.out.println("w pętli");
            System.out.println(MainActivity.tasks.get(i).toString());
            if (MainActivity.tasks.get(i).getId() == task.getId()) {
                MainActivity.tasks.set(i, task);
                break;
            }
        }

    }
}