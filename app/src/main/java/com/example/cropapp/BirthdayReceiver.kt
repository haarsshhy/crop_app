package com.example.cropapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BirthdayReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationHelper = NotificationHelper(context)
        notificationHelper.createNotificationChannel()
        notificationHelper.showSimpleNotification("Birthday Reminder", "It's your friend's birthday today!")
    }
}
