package com.toDoList.todolist_v20.notificationAlarm

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.toDoList.todolist_v20.R
import com.toDoList.todolist_v20.classes.BasicActivity
import com.toDoList.todolist_v20.dataBase.dbContent.MyIntentConstant
import com.toDoList.todolist_v20.objects.Variable

const val titleNotification = "titleExtra"
const val messageNotification = "messageExtra"

class Notification : BroadcastReceiver() {

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context, intent: Intent) {
        val intentBasicActivity = Intent(context, BasicActivity::class.java)

        intentBasicActivity.putExtra(MyIntentConstant.INTENT_TITLE_KEY, intent.getStringExtra(titleNotification))
        intentBasicActivity.putExtra(MyIntentConstant.INTENT_SUBTITLE_KEY, intent.getStringExtra(messageNotification))
        intentBasicActivity.putExtra(MyIntentConstant.INTENT_TAG_KEY, intent.getStringExtra(MyIntentConstant.INTENT_TAG_KEY))
        intentBasicActivity.putExtra(MyIntentConstant.INTENT_URL_KEY, intent.getStringExtra(MyIntentConstant.INTENT_URL_KEY))
        intentBasicActivity.putExtra(MyIntentConstant.INTENT_ID_KEY, Variable.id)
        intentBasicActivity.putExtra(MyIntentConstant.singInWithNotification,  true)


        val pendingIntent = PendingIntent.getActivity(context, 0, intentBasicActivity, PendingIntent.FLAG_ONE_SHOT)


        val notificationBuilder = NotificationCompat.Builder(context, Variable.channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleNotification))
            .setContentText(intent.getStringExtra(messageNotification))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(Variable.notificationID, notificationBuilder)
    }






}