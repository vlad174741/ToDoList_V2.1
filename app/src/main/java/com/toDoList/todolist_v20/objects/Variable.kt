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
    var imgURI = ""
    var id = 0
    var prevPositionRcView = -1
    var passwordCheck = false
    var password = ""
    var fingerPrintYes = false



    //Option
    var prefFingerPrint = 0
    var prefTheme = 0

    //Notification
    var notificationID = 0
    var channelID = ""
    var titleNotification = ""
    var messageNotification = ""
    var minute = 0
    var hour = 0
    var day = 0
    var month = 0
    var year = 0

}
