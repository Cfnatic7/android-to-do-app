package com.example.todoapp;
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Task implements Serializable {

    public long getId() {
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

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private Long id;
    private String title;
    private String description;
    private String creationTime;
    private String dueDate;
    private boolean completed;
    private boolean notificationEnabled;
    private String category;

    private List<String> attachments;


    public Task(Long id, String title, String description, String creationTime, String dueDate,
                boolean completed, boolean notificationEnabled, String category, ArrayList<String> attachments) {
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

    public Task() {

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
