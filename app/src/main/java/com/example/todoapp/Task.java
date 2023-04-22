package com.example.todoapp;
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Task implements Serializable {
    private final int id;
    private final String title;
    private final String description;
    private final String creationTime;
    private final String dueDate;
    private final boolean completed;
    private final boolean notificationEnabled;
    private final String category;

    private final ArrayList<Integer> attachments;

    public Task(int id, String title, String description, String creationTime, String dueDate,
                boolean completed, boolean notificationEnabled, String category, ArrayList<Integer> attachments) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationTime = creationTime;
        this.dueDate = dueDate;
        this.completed = completed;
        this.notificationEnabled = notificationEnabled;
        this.category = category;
        this.attachments = attachments;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isNotificationEnabled() {
        return notificationEnabled;
    }

    public String getCategory() {
        return category;
    }

    public ArrayList<Integer> getAttachments() {
        return attachments;
    }

    public boolean hasAttachments() {
        return attachments != null && !attachments.isEmpty();
    }

    @NonNull
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", creationTime='" + creationTime + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", completed=" + completed +
                ", notificationEnabled=" + notificationEnabled +
                ", category='" + category + '\'' +
                ", attachments=" + attachments +
                '}';
    }
}
