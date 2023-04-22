package com.example.todoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, NotificationService.class);
        Task task = (Task) intent.getSerializableExtra("task");
        System.out.println("on receive task title" + task.getTitle());
        serviceIntent.putExtra("task", task);
        ContextCompat.startForegroundService(context, serviceIntent);
    }
}

