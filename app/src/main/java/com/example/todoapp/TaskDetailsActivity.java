package com.example.todoapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        TextView taskTitle = findViewById(R.id.task_title);
        TextView taskDescription = findViewById(R.id.task_description);
        TextView taskCreationTime = findViewById(R.id.task_creation_time);
        TextView taskDueDate = findViewById(R.id.task_due_date);
        TextView taskStatus = findViewById(R.id.task_status);
        TextView taskCategory = findViewById(R.id.task_category);
        RecyclerView recyclerView = findViewById(R.id.attachment_recycler_view);

        Task task = (Task) getIntent().getSerializableExtra("task");

        taskTitle.setText(task.getTitle());
        taskDescription.setText(task.getDescription());
        taskCreationTime.setText(task.getCreationTime());
        taskDueDate.setText(task.getDueDate());
        taskStatus.setText(task.isCompleted() ? "Ukończony" : "Nieukończony");
        taskCategory.setText(task.getCategory());

        // Ustawienie RecyclerView dla załączników
        AttachmentAdapter attachmentAdapter = new AttachmentAdapter(new ArrayList<>(task.getAttachments()), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(attachmentAdapter);
    }
}