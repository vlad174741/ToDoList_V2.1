package com.toDoList.todolist_v20.objects

import android.annotation.SuppressLint
import com.toDoList.todolist_v20.dataBase.dbAuthorization.DataBaseManagerAuth
import com.toDoList.todolist_v20.dataBase.dbContent.DataBaseManager


object Variable {

    lateinit var dbManager: DataBaseManager
    @SuppressLint("StaticFieldLeak")
    lateinit var dbManagerAuth: DataBaseManagerAuth


    var check = 0
    var auth = false
    var username = "user"
    var imgURI = "empty"
    var id = 0
    var prevPositionRcView = -1
    var passwordCheck = false
    var password = ""
    var fingerPrintYes = false



    //Option
    var prefFingerPrint = 0
    var prefTheme = 0

    //Notification
    const val nameChanel = "Напоминания"
    const val descriptionChanel = "Уведомления с напоминанием"
    var notificationSingIn = false
    var dataNotification = ""
    var timeNotification = ""
    var notificationID = 0
    var channelID = "Напоминания"
    var minute = 0
    var hour = 0
    var day = 0
    var month = 0
    var year = 0

}
