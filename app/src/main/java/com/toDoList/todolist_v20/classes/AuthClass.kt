package com.toDoList.todolist_v20.classes

import android.annotation.SuppressLint
import android.content.Intent
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.toDoList.todolist_v20.R
import com.toDoList.todolist_v20.dataBase.dbContent.MyIntentConstant
import com.toDoList.todolist_v20.databinding.AuthPinFormBinding
import com.toDoList.todolist_v20.objects.Variable
import com.toDoList.todolist_v20.objects.Variable.dbManagerAuth


@SuppressLint("StaticFieldLeak")
lateinit var bindingAuth:AuthPinFormBinding
lateinit var intentMainActivity:Intent
class AuthClass: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        bindingAuth = AuthPinFormBinding.inflate(layoutInflater)
        setContentView(bindingAuth.root)




        Log.d("liveActivity", "AuthClass.onCreate")

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onStart() {
        super.onStart()
        Log.d("liveActivity", "AuthClass.onStart")
        intentMainActivity = Intent(this, MainActivity::class.java)

        if (!Variable.auth){
            dbManagerAuth.openDataBase()
            dbManagerAuth.checkAccount()

            if (Variable.auth){startActivity(intentMainActivity); finish()}
        }

        if(!Variable.passwordCheck){ checkBiometric()}
        fingerPrintButton()
        pinCodeButtons()


    }

    public override fun onResume() {
        super.onResume()
        Log.d("liveActivity", "AuthClass.onResume")

    }

    override fun onPause() {
        super.onPause()
        Log.d("liveActivity", "AuthClass.onPause")

    }

    override fun onStop() {
        super.onStop()
        dbManagerAuth.closeDataBase()
        if (Variable.auth) { finish() }
        Log.d("liveActivity", "AuthClass.onStop")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("liveActivity", "AuthClass.onDestroy")

    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun pinCodeButtons(){
        bindingAuth.apply {
            floatingActionButton1.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
            { textViewPin.append("1"); pinSingIn() })

            floatingActionButton2.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
            { textViewPin.append("2"); pinSingIn() })

            floatingActionButton3.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
            { textViewPin.append("3"); pinSingIn() })

            floatingActionButton4.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
            { textViewPin.append("4"); pinSingIn() })

            floatingActionButton5.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
            { textViewPin.append("5"); pinSingIn() })

            floatingActionButton6.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
            { textViewPin.append("6"); pinSingIn() })

            floatingActionButton7.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
            { textViewPin.append("7"); pinSingIn() })

            floatingActionButton8.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
            { textViewPin.append("8"); pinSingIn() })

            floatingActionButton9.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
            { textViewPin.append("9"); pinSingIn() })

            floatingActionButton0.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
            { textViewPin.append("0"); pinSingIn() })

            floatingActionButtonNo.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
            {
                if (textViewPin.text.isNotEmpty())
                {
                    val back = textViewPin.text.toString()
                    textViewPin.text = back.substring(0, back.length - 1)

                }else{textViewPin.append("")}
            })

            floatingActionButtonOk.setOnClickListener(@Suppress("UNUSED_PARAMETER") View.OnClickListener
            {
                if (Variable.passwordCheck){
                    if (textViewPin.length() == 4) {
                        Variable.password = textViewPin.text.toString()
                        bindingAuth.textViewCreatePin.visibility = View.GONE
                        Variable.passwordCheck = false
                        dbManagerAuth.insertOptionToDB()
                        checkBiometric()
                        fingerPrintButton()
                        textViewPin.text = ""
                    }else{
                        Toast.makeText(this@AuthClass, "Введите не менее 4 цифр", Toast.LENGTH_SHORT).show()

                    }

                } else { checkBiometric() }

            })

        }

    }


    private fun pinSingIn(){
        if(!Variable.passwordCheck) {
            if (bindingAuth.textViewPin.length() == 4) {

                if (bindingAuth.textViewPin.text.toString() == Variable.password) {
                    val intentMainActivity = Intent(this, MainActivity::class.java)
                    if (Variable.notificationSingIn){singInWithNotification()}
                    else{ startActivity(intentMainActivity) }
                    finish()
                } else {
                    bindingAuth.textViewPin.text = ""
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun fingerPrintButton(){


        if (!Variable.fingerPrintYes) {

            if (Variable.passwordCheck) {
                bindingAuth.textViewCreatePin.visibility = View.VISIBLE

                bindingAuth.floatingActionButtonOk.foreground =
                    ContextCompat.getDrawable(this, R.drawable.ic_number_button_yes)
            } else {
                bindingAuth.floatingActionButtonOk.visibility = View.GONE
            }
        }else{

            bindingAuth.floatingActionButtonOk.visibility = View.VISIBLE

            bindingAuth.floatingActionButtonOk.foreground =
                ContextCompat.getDrawable(this, R.drawable.ic_baseline_fingerprint)
        }

    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun checkBiometric() {
        val main = Intent(this@AuthClass, MainActivity::class.java)

        if (android.os.Build.VERSION.SDK_INT >= 28){

            val prompt = BiometricPrompt.Builder(this)
                .setTitle("Авторизация по отпечатку пальца")
                .setDescription("Используйте свой отпечаток для авторизации")
                .setNegativeButton("Отмена", this.mainExecutor
                ) { _, _ ->             Variable.fingerPrintYes = true; fingerPrintButton()
                }
                .build()

            prompt.authenticate(
                getCancellationSignal(),this.mainExecutor,
                object : BiometricPrompt.AuthenticationCallback() {

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(this@AuthClass, "Не распознан." , Toast.LENGTH_LONG).show()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                        super.onAuthenticationError(errorCode, errString)

                        if (errorCode != BiometricPrompt.BIOMETRIC_ERROR_NO_BIOMETRICS  &&
                            errorCode != BiometricPrompt.BIOMETRIC_ERROR_HW_UNAVAILABLE &&
                            errorCode != BiometricPrompt.BIOMETRIC_ERROR_HW_NOT_PRESENT &&
                            errorCode != BiometricPrompt.BIOMETRIC_ERROR_CANCELED &&
                            errorCode != BiometricPrompt.BIOMETRIC_ERROR_NO_DEVICE_CREDENTIAL
                        ) {Variable.fingerPrintYes = true; fingerPrintButton() }

                    }
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                        super.onAuthenticationSucceeded(result)
                        if (Variable.notificationSingIn){
                            singInWithNotification()
                        }else{
                            ContextCompat.startActivity(this@AuthClass, main, null)
                        }
                        Variable.auth = true
                    }
                }
            )
        }else{
            Variable.fingerPrintYes = false; fingerPrintButton()


        }
    }

    fun singInWithNotification(){

        val intentEditActivity = Intent(this, EditActivity::class.java)
        Variable.id = intent.getIntExtra(MyIntentConstant.INTENT_ID_KEY, 0)

        intentEditActivity.putExtra(MyIntentConstant.INTENT_TITLE_KEY, intent.getStringExtra(MyIntentConstant.INTENT_TITLE_KEY))
        intentEditActivity.putExtra(MyIntentConstant.INTENT_SUBTITLE_KEY, intent.getStringExtra(MyIntentConstant.INTENT_SUBTITLE_KEY))
        intentEditActivity.putExtra(MyIntentConstant.INTENT_TAG_KEY, intent.getStringExtra(MyIntentConstant.INTENT_TAG_KEY))
        intentEditActivity.putExtra(MyIntentConstant.singInWithNotification,  true)
        intentEditActivity.putExtra(MyIntentConstant.INTENT_ID_KEY, Variable.id)
        intentEditActivity.putExtra(MyIntentConstant.INTENT_URL_KEY, intent.getStringExtra(MyIntentConstant.INTENT_URL_KEY))

        startActivity(intentEditActivity)

    }


    private fun getCancellationSignal(): CancellationSignal {
        val cancelSignal = CancellationSignal()
        cancelSignal.setOnCancelListener {
        }
        return cancelSignal

    }


}


