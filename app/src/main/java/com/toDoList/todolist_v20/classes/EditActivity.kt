package com.toDoList.todolist_v20.classes

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.toDoList.todolist_v20.dataBase.dbContent.DataBaseManager
import com.toDoList.todolist_v20.dataBase.dbContent.MyIntentConstant
import com.toDoList.todolist_v20.databinding.ActivityEditBinding
import com.toDoList.todolist_v20.notificationAlarm.*
import com.toDoList.todolist_v20.objects.PhotoAndImage
import com.toDoList.todolist_v20.objects.Tags
import com.toDoList.todolist_v20.objects.ToastText
import com.toDoList.todolist_v20.objects.Variable
import java.util.*


@SuppressLint("StaticFieldLeak")
lateinit var bindingEdit: ActivityEditBinding
lateinit var tag: String
lateinit var imageName: String
lateinit var uriImageDb: Uri
lateinit var i: Intent
lateinit var hideKeyboardEditActivity: InputMethodManager
var notificationChange = false
@SuppressLint("StaticFieldLeak")
var focusedView: View? = null





class EditActivity: AppCompatActivity() {

    private var dataBaseManager = DataBaseManager(this)

    private val getResultCapturePhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        if (it.resultCode == Activity.RESULT_OK && it.data == null ) {
            uriImageDb = PhotoAndImage.uri
            bindingEdit.imageViewActivityEdit.setImageURI(uriImageDb)
            checkImage()
            PhotoAndImage.sendMessageGallery(this, PhotoAndImage.filePhoto)
            Variable.imgURI = uriImageDb.toString()
            Log.d("idDir", "save capture photo")
        }else{
            bindingEdit.imageViewActivityEdit.setImageURI(uriImageDb)
            checkImage()
            if (it.data?.data == null){
                    checkDeletePhoto()
                    Log.d("idDir", "delete capture photo")
            }
        }
        bindingEdit.floatingActionButtonAddPhotoEditActivity.isEnabled = true

    }

    private val getResultGalleryPhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        if (it.resultCode == Activity.RESULT_OK ) {
            Log.d("idDir", "save image gallery")
            bindingEdit.imageViewActivityEdit.setImageURI(it.data?.data)
            PhotoAndImage.uri = Uri.parse("")
            Variable.imgURI = it.data?.data.toString()
            checkImage()


        }
        bindingEdit.actionImageButton.visibility = View.GONE
        bindingEdit.floatingActionButtonAddImageEditActivity.isEnabled = true

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingEdit = ActivityEditBinding.inflate(layoutInflater)
        setContentView(bindingEdit.root)
        dataBaseManager.openDataBase()

        i = intent
        Variable.id = i.getIntExtra(MyIntentConstant.INTENT_ID_KEY,0)


        Tags.dbTag = i.getStringExtra(MyIntentConstant.INTENT_TAG_KEY).toString()


        Variable.notificationSingIn = i.getBooleanExtra(MyIntentConstant.singInWithNotification, false)


        tag = Tags.dbTag
        Tags.checkTagEditActivity(tag)


        uriImageDb = i.getStringExtra(MyIntentConstant.INTENT_URL_KEY).toString().toUri()
        Variable.imgURI = uriImageDb.toString()
        PhotoAndImage.uri = Uri.parse("")

        Log.d("notificationAlarm", "\n" + "id: " +
                Variable.id + "\n" + "tag: " + Tags.dbTag + "\n"  + "img: " + Variable.imgURI )



        bindingEdit.editTextEditActivityTitle.setText(i.getStringExtra(MyIntentConstant.INTENT_TITLE_KEY))
        bindingEdit.editTextEditActivitySubtitle.setText(i.getStringExtra(MyIntentConstant.INTENT_SUBTITLE_KEY))
        bindingEdit.imageViewActivityEdit.setImageURI(uriImageDb)
        imageName = bindingEdit.editTextEditActivityTitle.text.toString() + ".image"
        bindingEdit.timePickerNotificationCardEditActivity.setIs24HourView(true)


        hideKeyboardEditActivity = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        checkImage()
        buttonsEditActivity()
        Log.d("liveActivity", "EditActivity.onCreate")




    }

    override fun onStart() {
        super.onStart()
        Log.d("liveActivity", "EditActivity.onStart")

    }

    override fun onResume() {
        super.onResume()
        Log.d("liveActivity", "EditActivity.onResume")

    }

    override fun onPause() {
        super.onPause()
        Log.d("liveActivity", "EditActivity.onPause")

    }

    override fun onStop() {
        super.onStop()
        Log.d("liveActivity", "EditActivity.onStop")

    }

    override fun onRestart() {
        super.onRestart()
        Log.d("liveActivity", "EditActivity.onRestart")

    }

    override fun onDestroy() {
        super.onDestroy()
        Variable.imgURI = "empty"
        uriImageDb = Uri.parse("")
        PhotoAndImage.uri = Uri.parse("")
        Tags.dbTag = "empty"
        dataBaseManager.closeDataBase()
        Log.d("liveActivity", "EditActivity.onDestroy")


    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun buttonsEditActivity(){

        bindingEdit.apply {
            //Кнопка для сохранения заметки
            buttonSaveEditActivity.setOnClickListener {
                if(editTextEditActivityTitle.text.isNotEmpty()) {
                    buttonSaveEditActivity.isEnabled = false
                    val title = editTextEditActivityTitle.text.toString()
                    val subtitle = editTextEditActivitySubtitle.text.toString()

                    checkSavePhoto()
                    dataBaseManager.updateToDataBase(
                        title,
                        subtitle,
                        Variable.id,
                        saveTag(),
                        Variable.imgURI
                    )
                    if (notificationChange) {scheduleNotification()}

                    Variable.imgURI = "empty"
                    uriImageDb = Uri.parse("")
                    PhotoAndImage.uri = Uri.parse("")
                    Tags.dbTag = "empty"
                    closeActivityWithNotification()
                }else{ ToastText.shortToast(this@EditActivity, "Введите заголовок") }

            }

            //Кнопка для выбора действий для изображения
            floatingActionButtonActionImageEditActivity.setOnClickListener {

                    if (actionImageButton.visibility == View.GONE) {
                        actionImageButton.visibility = View.VISIBLE
                    }else{
                        actionImageButton.visibility = View.GONE
                    }
            }

            //Кнопка для добавления изображения через галерею
            floatingActionButtonAddImageEditActivity.setOnClickListener {
                floatingActionButtonAddImageEditActivity.isEnabled = false
                PhotoAndImage.chooseImageGallery(getResultGalleryPhoto)
            }
            //Кнопка для добавления изображения через камеру
            floatingActionButtonAddPhotoEditActivity.setOnClickListener {
                floatingActionButtonAddPhotoEditActivity.isEnabled = false
                checkPermissions()
            }

            comeBackImageViewEditActivity.setOnClickListener{
                bindingEdit.cardViewNotificationTimerEditActivity.visibility = View.GONE
            }

            //Кнопка для добавления и редактирования уведомлений
            floatingActionButtonAddNotificationTimerEditActivity2.setOnClickListener {
                focusedView = currentFocus

                if(editTextEditActivityTitle.text.isNotEmpty()) {

                    if (Variable.notificationID != 0) {
                        AlertData.showAlertToUpdateNotification(
                            Variable.timeNotification,
                            Variable.dataNotification, this@EditActivity, true
                        )
                    } else {
                        bindingEdit.cardViewNotificationTimerEditActivity.visibility = View.VISIBLE

                    }


                }else{ ToastText.shortToast(this@EditActivity, "Введите заголовок") }

                }

            //Кнопка для применения выбранных даты и времени

            buttonNotificationCardEditActivity.setOnClickListener {


                    Variable.minute = bindingEdit.timePickerNotificationCardEditActivity.minute
                    Variable.hour = timePickerNotificationCardEditActivity.hour
                    Variable.day = datePickerNotificationCardEditActivity.dayOfMonth
                    Variable.month = datePickerNotificationCardEditActivity.month
                    Variable.year = datePickerNotificationCardEditActivity.year

                val time = AlertData.getTime()
                val date = Date(time)
                val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
                val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)
                Variable.dataNotification = dateFormat.format(date)
                Variable.timeNotification = timeFormat.format(date)
                notificationChange = true
                bindingEdit.cardViewNotificationTimerEditActivity.visibility = View.GONE
                AlertData.showAlertToUpdateNotification(Variable.timeNotification,
                    Variable.dataNotification, this@EditActivity, false)
            }



            //Кнопка для удаления изображения
            floatingActionButtonDeletePhotoEditActivity.setOnClickListener {
                alertDeleteDialog()
            }


            //Кнопки для фильтров

            textViewTagCardEditActivity.setOnClickListener {
                if (scrollTagWindowEditActivity.visibility == View.GONE) {
                    scrollTagWindowEditActivity.visibility = View.VISIBLE
                } else {
                    scrollTagWindowEditActivity.visibility = View.GONE
                }
            }
            radioButtonEditActivityTagHome.setOnClickListener {
                checkTagSelection(radioButtonEditActivityTagHome, Tags.homeTag)
            }
            radioButtonEditActivityTagShop.setOnClickListener {
                checkTagSelection(radioButtonEditActivityTagShop, Tags.shopTag)
            }
            radioButtonEditActivityTagBank.setOnClickListener {
                checkTagSelection(radioButtonEditActivityTagBank, Tags.bankTag)
            }
            radioButtonEditActivityTagWork.setOnClickListener {
                checkTagSelection(radioButtonEditActivityTagWork, Tags.workTag)
            }
            radioButtonEditActivityTagWeekend.setOnClickListener {
                checkTagSelection(radioButtonEditActivityTagWeekend, Tags.weekendTag)
            }
            radioButtonEditActivityTagSport.setOnClickListener {
                checkTagSelection(radioButtonEditActivityTagSport, Tags.sportTag)
            }
            radioButtonEditActivityTagClear.setOnClickListener {
                checkTagSelection(radioButtonEditActivityTagClear, "empty")
            }


        }

    }

    fun closeActivityWithNotification(){
        if (Variable.notificationSingIn){
            var intent = Intent(this@EditActivity, MainActivity::class.java)
            i.putExtra(MyIntentConstant.singInWithNotification,  false)
            Variable.notificationSingIn = false
            startActivity(intent)
            finish()
        }
        else{finish()}
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun scheduleNotification()
    {

        val intent = Intent(applicationContext, Notification::class.java)


        intent.putExtra(titleNotification,  bindingEdit.editTextEditActivityTitle.text.toString())
        intent.putExtra(messageNotification, bindingEdit.editTextEditActivitySubtitle.text.toString())
        intent.putExtra(MyIntentConstant.INTENT_TAG_KEY, Tags.dbTag)
        intent.putExtra(MyIntentConstant.INTENT_URL_KEY, Variable.imgURI)



        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            Variable.notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = AlertData.getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }


    fun showNotificationWindow() {
        focusedView?.let { hideKeyboardEditActivity.hideSoftInputFromWindow(it.windowToken, 0) }
        bindingEdit.cardViewNotificationTimerEditActivity.visibility = View.VISIBLE
    }


    private fun alertDeleteDialog(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Удалить")
        alertDialog.setMessage("Действительно хотите удалить изоброжение из заметки?")
        alertDialog.setPositiveButton("Удалить"){_,_ ->
            bindingEdit.apply {
                Variable.imgURI = ""
                imageViewActivityEdit.setImageDrawable(null)
                cardViewImageActivityEdit.visibility = View.GONE
                actionImageButton.visibility = View.GONE
            }
        }
        alertDialog.setNegativeButton("Нет"){_,_ ->
            bindingEdit.actionImageButton.visibility = View.GONE
        }
        alertDialog.show()
    }


    private fun checkImage() {
        bindingEdit.apply {
            if (imageViewActivityEdit.drawable != null) {
                floatingActionButtonDeletePhotoEditActivity.visibility = View.VISIBLE
                cardViewImageActivityEdit.visibility = View.VISIBLE
                actionImageButton.visibility = View.GONE
            }else{
                floatingActionButtonDeletePhotoEditActivity.visibility = View.GONE
                cardViewImageActivityEdit.visibility = View.GONE
                actionImageButton.visibility = View.GONE
            }
        }
    }


    private fun checkTagSelection(button: RadioButton, tag: String){
        Tags.tagSelectEdit(button, tag, bindingEdit.textViewTagCardEditActivity)
    }


    private fun checkSavePhoto(){

        if (PhotoAndImage.uri == "".toUri()){
            if(Variable.imgURI == "") {
                Variable.imgURI = "empty"
                PhotoAndImage.uri = Uri.parse("")
            }
        }else{
            Variable.imgURI = PhotoAndImage.uri.toString()
        }
    }

    private fun checkDeletePhoto(){
        if (PhotoAndImage.uri != "".toUri()){
            PhotoAndImage.deletePhoto(this, PhotoAndImage.uri)
            PhotoAndImage.uri = Uri.parse("")
            uriImageDb = Uri.parse("")
            PhotoAndImage.sendMessageGallery(this, PhotoAndImage.filePhoto)
        }else{
            if (uriImageDb != "".toUri()){
            PhotoAndImage.deletePhoto(this, uriImageDb)
                if (PhotoAndImage.uri != "".toUri()) {
                    val f = uriImageDb.toFile()
                    PhotoAndImage.sendMessageGallery(this, f)
                    PhotoAndImage.uri = Uri.parse("")
                }else{
                    Log.d("idDir", "image data base is empty")
                }
            }else{
                Log.d("idDir", "image data base is empty")
            }
        }
    }

    private fun saveTag(): String{

        tag = if(Tags.dbTag != "empty") { Tags.dbTag }
        else {
            if (Tags.dbTag == "empty") { "empty" }
            else { i.getStringExtra(MyIntentConstant.INTENT_TAG_KEY).toString() }
        }
        Log.d("idDir", "save tag:$tag")

        return tag
    }


    private fun checkPermissions() {
        if (this.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } != PackageManager.PERMISSION_GRANTED) {
            Log.d("idDir", "Request Permissions")
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ))
        } else {
            Log.d("idDir", "Permission Already Granted")
            PhotoAndImage.takeFullPhoto(getResultCapturePhoto, imageName)
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                permissions -> permissions.entries.forEach {
                Log.d("idDir", "${it.key} = ${it.value}")
            }
            if (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true &&
                permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true) {
                Log.d("idDir", "Permission granted")
                PhotoAndImage.takeFullPhoto(getResultCapturePhoto, imageName)

            } else {
                Log.d("idDir", "Permission not granted")
            }
        }

}