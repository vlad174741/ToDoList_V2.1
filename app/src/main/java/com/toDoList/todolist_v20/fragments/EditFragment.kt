package com.toDoList.todolist_v20.fragments

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.toDoList.todolist_v20.classes.ViewModelMy
import com.toDoList.todolist_v20.classes.bindingEdit
import com.toDoList.todolist_v20.dataBase.dbContent.DataBaseManager
import com.toDoList.todolist_v20.dataBase.dbContent.MyIntentConstant
import com.toDoList.todolist_v20.dataClass.Data
import com.toDoList.todolist_v20.databinding.FragmentEditBinding
import com.toDoList.todolist_v20.notificationAlarm.*
import com.toDoList.todolist_v20.notificationAlarm.Notification
import com.toDoList.todolist_v20.objects.PhotoAndImage
import com.toDoList.todolist_v20.objects.Tags
import com.toDoList.todolist_v20.objects.ToastText
import com.toDoList.todolist_v20.objects.Variable
import com.toDoList.todolist_v20.objects.Variable.dbManager
import com.toDoList.todolist_v20.objects.Variable.minute
import java.util.*


@SuppressLint("StaticFieldLeak")
lateinit var bindingEditFragment: FragmentEditBinding
@SuppressLint("StaticFieldLeak")
private lateinit var contextEditFragment: Context
lateinit var hideKeyboard: InputMethodManager






class EditFragment : Fragment() {
    private val model: ViewModelMy by activityViewModels()

    private val getResultCapturePhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        if (it.resultCode == Activity.RESULT_OK && it.data == null ) {
            bindingEditFragment.imageViewEditFragment.setImageURI(PhotoAndImage.uri)
            checkImage()
            PhotoAndImage.sendMessageGallery(contextEditFragment, PhotoAndImage.filePhoto)
            Variable.imgURI = PhotoAndImage.uri.toString()
            Log.d("idDir", "save capture photo")
        }else{
            bindingEditFragment.imageViewEditFragment.setImageURI(it.data?.data)
            checkImage()
            if (it.data?.data == null){
                checkSavePhoto()
                Log.d("idDir", "delete capture photo")
            }
        }
        bindingEditFragment.floatingActionButtonAddPhotoEditFragment.isEnabled = true
    }

    private val getResultGalleryPhoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        if (it.resultCode == Activity.RESULT_OK ) {
            Log.d("idDir", "save image gallery")
            bindingEditFragment.imageViewEditFragment.setImageURI(it.data?.data)
            PhotoAndImage.uri = Uri.parse("")
            Variable.imgURI = it.data?.data.toString()
            checkImage()


        }
        bindingEditFragment.actionImageButtonEditFragment.visibility = View.GONE
        bindingEditFragment.floatingActionButtonAddImageEditFragment.isEnabled = true

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("liveFragment", "EditFragment.onAttach")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("liveFragment", "EditFragment.onCreate")
        contextEditFragment = activity as AppCompatActivity


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        bindingEditFragment = FragmentEditBinding.inflate(inflater, container, false)
        Log.d("liveFragment", "EditFragment.onCreateView")
        return bindingEditFragment.root
    }




    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbManager = DataBaseManager(contextEditFragment)
        hideKeyboard = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        bindingEditFragment.timePicker.setIs24HourView(true)
        dbManager.openDataBase()
        checkImage()
        buttonsEditFragment()
        Log.d("liveFragment", "EditFragment.onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("liveFragment", "EditFragment.onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("liveFragment", "EditFragment.onResume")
        if(rcAdapter.isEnable){ rcAdapter.clearItemSelect()}
    }

    override fun onPause() {
        super.onPause()
        Log.d("liveFragment", "EditFragment.onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("liveFragment", "EditFragment.onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("liveFragment", "EditFragment.onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("liveFragment", "EditFragment.onDestroy")
    }


    override fun onDetach() {
        super.onDetach()
        Log.d("liveFragment", "EditFragment.onDetach")
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun buttonsEditFragment(){

        bindingEditFragment.apply {

            textViewTagCardEditFragment.setOnClickListener {

                radioGroupTegEditFragment.clearCheck()
                textViewTagCardEditFragment.text = "?????? ??????????????"
                Tags.dbTag = "empty"

                if (scrollTagWindowEditFragment.visibility == View.GONE) {
                    scrollTagWindowEditFragment.visibility = View.VISIBLE
                }else{
                    scrollTagWindowEditFragment.visibility = View.GONE
                }
            }

            //???????????? ?????? ???????????? ????????????????
            floatingActionButtonActionEditFragment.setOnClickListener {
                if (actionImageButtonEditFragment.visibility == View.GONE) {
                    actionImageButtonEditFragment.visibility = View.VISIBLE
                }else{
                    actionImageButtonEditFragment.visibility = View.GONE
                }
            }


            //???????????? ?????? ???????????????????? ?????????????????????? ?? ????????????????
            floatingActionButtonAddNotificationTimerEditActivity.setOnClickListener {
                val title = editTextEditFragmentTitle.text

                if (title.isEmpty()){
                    ToastText.shortToast(contextEditFragment, "?????????????? ??????????????????")
                }else {
                    hideKeyboard.hideSoftInputFromWindow(requireView().windowToken, 0)
                    cardViewNotificationTimer.visibility = View.VISIBLE
                    Log.d("notificationAlarm", "${Variable.notificationID}")
                }
            }

            comeBackImageViewEditFragment.setOnClickListener{
                cardViewNotificationTimer.visibility = View.GONE
            }

            //???????????? ?????? ???????????????????? ?????????????????????? ?????????? ??????????????
            floatingActionButtonAddImageEditFragment.setOnClickListener {
                floatingActionButtonAddImageEditFragment.isEnabled = false
                PhotoAndImage.chooseImageGallery(getResultGalleryPhoto)
            }
            //???????????? ?????? ???????????????????? ?????????????????????? ?????????? ????????????
            floatingActionButtonAddPhotoEditFragment.setOnClickListener {
                floatingActionButtonAddPhotoEditFragment.isEnabled = false
                checkPermissions()
            }
            //???????????? ?????? ?????????????? ???????? ?? ????????????????????????
            floatingActionButtonDeletePhotoEditFragment.setOnClickListener {
                checkSavePhoto()
                cardViewImageEditFragment.visibility = View.GONE
                imageViewEditFragment.setImageDrawable(null)
                checkImage()
                Variable.imgURI = "empty"
            }
            radioButtonEditFragmentTagHome.setOnClickListener {
                checkTag(radioButtonEditFragmentTagHome, Tags.homeTag)
            }
            radioButtonEditFragmentTagShop.setOnClickListener {
                checkTag(radioButtonEditFragmentTagShop, Tags.shopTag)
            }
            radioButtonEditFragmentTagBank.setOnClickListener {
                checkTag(radioButtonEditFragmentTagBank, Tags.bankTag)
            }
            radioButtonEditFragmentTagWork.setOnClickListener {
                checkTag(radioButtonEditFragmentTagWork, Tags.workTag)
            }
            radioButtonEditFragmentTagWeekend.setOnClickListener {
                checkTag(radioButtonEditFragmentTagWeekend, Tags.weekendTag)
            }
            radioButtonEditFragmentTagSport.setOnClickListener {
                checkTag(radioButtonEditFragmentTagSport, Tags.sportTag)
            }


            //???????????? ?????? ?????????????????? ?????????????? ?????? ??????????????

            buttonSelectNotificationTimer.setOnClickListener{
                val title = editTextEditFragmentTitle.text.toString()

                minute = timePicker.minute
                Variable.hour = timePicker.hour
                Variable.day = datePicker.dayOfMonth
                Variable.month = datePicker.month
                Variable.year = datePicker.year
                cardViewNotificationTimer.visibility = View.GONE
                val time = AlertData.getTime()
                val date = Date(time)
                val dateFormat = android.text.format.DateFormat.getLongDateFormat(ContextWrapper(context).applicationContext)
                val timeFormat = android.text.format.DateFormat.getTimeFormat(ContextWrapper(context).applicationContext)
                Variable.dataNotification = dateFormat.format(date)
                Variable.timeNotification = timeFormat.format(date)
                Variable.notificationID = (title.length + (100..1000).random() + dbManager.getLastOrFirstId(-1) + dbManager.getLastOrFirstId(1))

                Log.d("notificationAlarm", "id: ${Variable.notificationID} | minute: $minute")


            }


            //???????????? ?????? ???????????????????? ??????????????
            buttonSaveEditFragment.setOnClickListener{

                val title = editTextEditFragmentTitle.text
                val subtitle = editTextEditFragmentSubtitle.text

                if (title.isEmpty()){
                    ToastText.shortToast(contextEditFragment, "?????????????? ??????????????????")
                }else{
                    ToastText.shortToast(contextEditFragment, "??????????????????")
                    hideKeyboard.hideSoftInputFromWindow(requireView().windowToken, 0)
                    Variable.id = dbManager.getLastOrFirstId(-1) + 1


                    dbManager.insertToDataBase(
                        title.toString(),subtitle.toString(), Tags.dbTag, Variable.imgURI, Variable.notificationID)



                    if(Variable.notificationID != 0) {
                        scheduleNotification(title.toString(), subtitle.toString())
                        Variable.notificationID = 0

                    }

                    PhotoAndImage.uri = Uri.parse("")
                    Variable.imgURI = "empty"
                    Tags.dbTag = "empty"
                    title.clear()
                    subtitle.clear()

                    cardViewImageEditFragment.visibility = View.GONE
                    imageViewEditFragment.setImageDrawable(null)
                    checkImage()
                    textViewTagCardEditFragment.text = "?????? ??????????????"
                    scrollTagWindowEditFragment.visibility = View.GONE
                    radioGroupTegEditFragment.clearCheck()
                    scrollViewEditFragment.smoothScrollTo(0, textViewTagCardEditFragment.top)

                    MainFragment().scrollRcView()
                }
                model.plant.value = Data("Info22", "UseCase22")
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun scheduleNotification(titleExtra:String, messageExtra:String)
    {

        val intent = Intent(ContextWrapper(contextEditFragment).applicationContext, Notification::class.java)


        intent.putExtra(titleNotification, titleExtra)
        intent.putExtra(messageNotification, messageExtra)
        intent.putExtra(MyIntentConstant.INTENT_TAG_KEY, Tags.dbTag)
        intent.putExtra(MyIntentConstant.INTENT_URL_KEY, Variable.imgURI)


        val pendingIntent = PendingIntent.getBroadcast(
            ContextWrapper(contextEditFragment).applicationContext,
            Variable.notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = AlertData.getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
        AlertData.showAlert(time, titleExtra, messageExtra, contextEditFragment)
    }




    private fun takeAndSavePhoto(){
        PhotoAndImage.takeFullPhoto(getResultCapturePhoto, PhotoAndImage.FILE_NAME)
    }

    private fun checkSavePhoto(){
        if (PhotoAndImage.uri != "".toUri()){
            PhotoAndImage.deletePhoto(contextEditFragment, PhotoAndImage.uri)
            PhotoAndImage.sendMessageGallery(contextEditFragment, PhotoAndImage.filePhoto)
            PhotoAndImage.uri = Uri.parse("")
            bindingEditFragment.cardViewImageEditFragment.visibility = View.GONE


        }
        if (bindingEditFragment.imageViewEditFragment.drawable == null) {
            bindingEditFragment.cardViewImageEditFragment.visibility = View.GONE
        }
    }


    private fun checkImage() {
        bindingEditFragment.apply {
            if (imageViewEditFragment.drawable != null) {
                cardViewImageEditFragment.visibility = View.VISIBLE
                floatingActionButtonDeletePhotoEditFragment.visibility = View.VISIBLE
                actionImageButtonEditFragment.visibility = View.GONE

            }else{
                cardViewImageEditFragment.visibility = View.GONE
                actionImageButtonEditFragment.visibility = View.GONE
                floatingActionButtonDeletePhotoEditFragment.visibility = View.GONE

            }
        }
    }

    private fun checkTag(button: RadioButton, tag: String){
        Tags.tagSelectEdit(button, tag, bindingEditFragment.textViewTagCardEditFragment)
    }

    private fun checkPermissions() {
        if (contextEditFragment.let {
                ContextCompat.checkSelfPermission(
                    it,
                    READ_EXTERNAL_STORAGE
                )
            } != PackageManager.PERMISSION_GRANTED) {
            Log.d("idDir", "Request Permissions")
            requestMultiplePermissions.launch(
                arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE))
        } else {
            Log.d("idDir", "Permission Already Granted")
            takeAndSavePhoto()
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("idDir", "${it.key} = ${it.value}")
            }
            if (permissions[READ_EXTERNAL_STORAGE] == true && permissions[WRITE_EXTERNAL_STORAGE] == true) {
                Log.d("idDir", "Permission granted")
                takeAndSavePhoto()
            } else {
                Log.d("idDir", "Permission not granted")
            }
        }


    companion object {
        @JvmStatic
        fun newInstance() = EditFragment()
    }
}