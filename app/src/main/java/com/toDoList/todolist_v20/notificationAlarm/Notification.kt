package com.toDoList.todolist_v20.notificationAlarm

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.toDoList.todolist_v20.R
import com.toDoList.todolist_v20.objects.Variable


class Notification : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationBuilder = NotificationCompat.Builder(context, Variable.channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(Variable.titleNotification)
            .setContentText(Variable.messageNotification)
            .build()

        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(Variable.notificationID, notificationBuilder)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(activity: Activity)
    {
        val name = Variable.titleNotification
        val desc = Variable.titleNotification
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(Variable.channelID, name, importance)
        channel.description = desc
        val notificationManager = activity.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }




}