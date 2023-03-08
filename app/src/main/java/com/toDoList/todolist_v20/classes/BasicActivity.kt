package com.toDoList.todolist_v20.classes

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.toDoList.todolist_v20.dataBase.dbAuthorization.DataBaseManagerAuth
import com.toDoList.todolist_v20.dataBase.dbContent.MyIntentConstant
import com.toDoList.todolist_v20.objects.SharedPreference
import com.toDoList.todolist_v20.objects.Variable

open class BasicActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Variable.dbManagerAuth = DataBaseManagerAuth(this)
        Variable.notificationSingIn = intent.getBooleanExtra(MyIntentConstant.singInWithNotification, false)

        checkUser()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkUser(){

        SharedPreference.preferenceUsername(this)
        Variable.dbManagerAuth.openDataBase()
        Variable.dbManagerAuth.checkAccount()

        when(Variable.prefTheme){
            0-> ChangeTheme().themeChange(0, delegate)
            1-> ChangeTheme().themeChange(1, delegate)
            2-> ChangeTheme().themeChange(2, delegate)
        }

        if (Variable.auth){
            finish()
            val intentMainActivity = Intent(this, MainActivity::class.java)
            val intentEditActivity = Intent(this, EditActivity::class.java)
            if (Variable.notificationSingIn){
                startActivity(intentEditActivity)
            }
            else {startActivity(intentMainActivity)}
            Log.d("liveActivity", "BasicActivity.open.MainActivity")

        }else{
            finish()
            val intentAuthClass = Intent(this, AuthClass::class.java)
            startActivity(intentAuthClass)
            Log.d("liveActivity", "BasicActivity.open.AuthClass")

        }
    }


}