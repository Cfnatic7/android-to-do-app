package com.example.todoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String taskJson = intent.getStringExtra("task");
        System.out.println(taskJson);

        Intent serviceIntent = new Intent(context, NotificationService.class);
        serviceIntent.putExtra("task", taskJson);
        ContextCompat.startForegroundService(context, serviceIntent);
    }

}

