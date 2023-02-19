package com.toDoList.todolist_v20.notificationAlarm

import android.app.*
import android.content.Context
import android.content.ContextWrapper
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
    fun getTime(): Long {


        val calendar = Calendar.getInstance()
        calendar.set(Variable.year, Variable.month, Variable.day, Variable.hour, Variable.minute)
        return calendar.timeInMillis
    }




}