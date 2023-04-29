package com.example.todoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);
        }
    }
    private CheckBox hideCompletedTasksCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

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

        Button btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
