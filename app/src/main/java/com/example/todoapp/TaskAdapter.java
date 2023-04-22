package com.example.todoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final List<Task> tasks;
    private final OnTaskClickListener onTaskClickListener;

    public TaskAdapter(List<Task> tasks, OnTaskClickListener onTaskClickListener) {
        this.tasks = tasks;
        this.onTaskClickListener = onTaskClickListener;
    }

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskDescription, taskCreationTime, taskDueDate, taskStatus, taskNotification, taskCategory;

        ImageView attachmentIndicator;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            taskCreationTime = itemView.findViewById(R.id.taskCreationTime);
            taskDueDate = itemView.findViewById(R.id.taskDueDate);
            taskStatus = itemView.findViewById(R.id.taskStatus);
            taskNotification = itemView.findViewById(R.id.taskNotification);
            taskCategory = itemView.findViewById(R.id.taskCategory);
            attachmentIndicator = itemView.findViewById(R.id.attachmentIndicator);
        }

        public void bind(final Task task) {
            taskTitle.setText(task.getTitle());
            taskDescription.setText(task.getDescription());
            taskCreationTime.setText(task.getCreationTime());
            taskDueDate.setText(task.getDueDate());
            taskStatus.setText(task.isCompleted() ? "Completed" : "Not Completed");
            taskNotification.setText(task.isNotificationEnabled() ? "Notification Enabled" : "Notification Disabled");
            taskCategory.setText(task.getCategory());
            attachmentIndicator.setVisibility(task.hasAttachments() ? View.VISIBLE : View.GONE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTaskClickListener.onTaskClick(task);
                }
            });
        }
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
