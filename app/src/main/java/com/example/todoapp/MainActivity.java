package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TaskAdapter taskAdapter;

    private List<Task> initData() {
        // This list is just an example. Instead, load data from the device's database.
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1, "Buy groceries", "Buy milk, eggs, and bread", "2023-04-22 10:00", "2023-04-25 12:00", false, true, "Shopping"));
        tasks.add(new Task(2, "Finish project", "Finish the project by the end of the week", "2023-04-22 11:00", "2023-04-29 18:00", false, true, "Work"));
        tasks.add(new Task(3, "Pay bills", "Pay the electricity and water bills", "2023-04-22 12:00", "2023-04-30 23:59", false, false, "Finance"));
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

        initRecyclerView();
    }
}