package com.example.todoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private CheckBox hideCompletedTasksCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        hideCompletedTasksCheckBox = findViewById(R.id.hide_completed_tasks_checkbox);

        SharedPreferences sharedPreferences = getSharedPreferences("app_preferences", MODE_PRIVATE);
        boolean hideCompletedTasks = sharedPreferences.getBoolean("hide_completed_tasks", false);
        hideCompletedTasksCheckBox.setChecked(hideCompletedTasks);

        hideCompletedTasksCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("hide_completed_tasks", hideCompletedTasksCheckBox.isChecked());
                editor.apply();
            }
        });
    }
}
