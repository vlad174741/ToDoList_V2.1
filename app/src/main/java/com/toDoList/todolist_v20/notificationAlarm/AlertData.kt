package com.toDoList.todolist_v20.notificationAlarm

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.ContextWrapper
import android.os.Build
import androidx.annotation.RequiresApi
import com.toDoList.todolist_v20.classes.EditActivity
import com.toDoList.todolist_v20.objects.Variable
import java.util.*


object AlertData {




    fun showAlert(time: Long, title: String, message: String, context: Context)
    {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(ContextWrapper(context).applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(ContextWrapper(context).applicationContext)

        AlertDialog.Builder(context)
            .setTitle("Уведомление")
            .setMessage(
                "ЗАГОЛОВОК: " + title +
                        "\nСообщение: " + message +
                        "\nКогда: " + dateFormat.format(date) + " " + timeFormat.format(date))
            .setPositiveButton("ОК"){_,_ ->}
            .show()
    }

    fun showAlertToUpdateNotification( time: String, date: String, context: Context, showButton: Boolean)
    {

        if (showButton) {

            AlertDialog.Builder(context)
                .setTitle("Уведомление уже имеет дату и время. Хотите изменить их?")
                .setMessage(

                            "\n Дата:" + " " + date +
                            "\n Время:" + " " + time
                )

                .setPositiveButton("Да") { _, _ -> EditActivity().showNotificationWindow() }
                .setNegativeButton("Нет") { _, _ -> }
                .show()
        }else{
            AlertDialog.Builder(context)
                .setTitle("Дата и время для уведомления установлены. ")
                .setMessage(
                            "Не забудьте сохранить заметку" +
                                    "\n" +
                            "\n Дата:" + " " + date +
                            "\n Время:" + " " + time
                )
                .setPositiveButton("Ок") { _, _ -> }
                .show()

        }
    }
    fun getTime(): Long {


        val calendar = Calendar.getInstance()
        calendar.set(Variable.year, Variable.month, Variable.day, Variable.hour, Variable.minute)
        return calendar.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context)
    {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(Variable.channelID, Variable.nameChanel, importance)
        channel.description = Variable.descriptionChanel
        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }




}