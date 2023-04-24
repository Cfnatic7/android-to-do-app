package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class EditTaskActivity extends AddTaskActivity {
    private Task taskToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("edit task");

        Intent intent = getIntent();
        taskToEdit = (Task) intent.getSerializableExtra("task");

        if (taskToEdit != null) {
            taskTitleEditText.setText(taskToEdit.getTitle());
            taskDescriptionEditText.setText(taskToEdit.getDescription());
            // Ustaw datę i czas z taskToEdit
            // Ustaw status i powiadomienia z taskToEdit
            taskStatusCheckBox.setChecked(taskToEdit.isCompleted());
            taskNotificationCheckBox.setChecked(taskToEdit.isNotificationEnabled());
            taskCategoryEditText.setText(taskToEdit.getCategory());
            attachments = (ArrayList<String>) taskToEdit.getAttachments();
        }
    }

    @Override
    protected void saveTask(Long id) {
        cancelNotification(taskToEdit);
        System.out.println("Saving task in edition: " + taskToEdit.toString());
        super.saveTask( taskToEdit.getId());
        Intent resultIntent = new Intent();
        resultIntent.putExtra("task", taskToEdit);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
