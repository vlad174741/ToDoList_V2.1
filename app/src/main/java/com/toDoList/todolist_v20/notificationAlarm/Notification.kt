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

const val titleNotification = "titleExtra"
const val messageNotification = "messageExtra"

class Notification : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationBuilder = NotificationCompat.Builder(context, Variable.channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleNotification))
            .setContentText(intent.getStringExtra(messageNotification))
            .build()

        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(Variable.notificationID, notificationBuilder)
    }






}