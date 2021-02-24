package com.example.remindbuddy

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.work.*
import com.example.remindbuddy.db.AppDatabase
import com.example.remindbuddy.db.Reminder
import com.example.remindbuddy.ui.home.HomeFragment
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class AddTaskActivity : AppCompatActivity() {
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var isUpdate = false
    private var uid = 0
    private val RQ_SPEECH_REC = 102
    private val RQ_SPEECH_DEC = 103
    private var savedimg = ""
    private var savedicon = ""
    private var selectedIcon = ""
    private var calyear = 0
    private var calmonth = 0
    private var calday = 0
    private var hourOfDay = 0
    private var minuteOfHour = 0
    private lateinit var geofencingClient: GeofencingClient


    val MONTHS = listOf<String>(
        "Jan",
        "Feb",
        "March",
        "Apr",
        "May",
        "June",
        "July",
        "Aug",
        "Sept",
        "Oct",
        "Nov",
        "Dec"
    )
    lateinit var imageView: ImageView
    lateinit var addImage: TextView
    lateinit var title : EditText
    lateinit var description: EditText
    lateinit var deleteimagebtn: Button
    private var imagestr = ""
    lateinit var iconimg: ImageView
    lateinit var addTaskText: TextView
    var lat =""
    var lng = ""
    var latlang: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        geofencingClient = LocationServices.getGeofencingClient(this)

        title = findViewById(R.id.editTextTextPersonName5)
        description = findViewById<EditText>(R.id.editTextTextMultiLine)
        val location = findViewById<EditText>(R.id.editTextTextPersonName6)


        val dateText = findViewById<EditText>(R.id.editTextDate)
        val timeText = findViewById<EditText>(R.id.editTextTime)
        val cancelbtn = findViewById<Button>(R.id.addtaskbtn2)
        val addReminder = findViewById<Button>(R.id.addtaskbtn)

        imageView = findViewById(R.id.imageView)
        addImage = findViewById(R.id.addimagetxt)
        deleteimagebtn = findViewById(R.id.deletebtn)
        deleteimagebtn.visibility = View.GONE
        iconimg = findViewById(R.id.imageView2)

        addTaskText = findViewById(R.id.textView15)


        //speech recog
        val speechbutton = findViewById<Button>(R.id.speechbtn)
        speechbutton.setOnClickListener {
            askSpeechInput()
        }

        location.setOnClickListener {
            startActivity(Intent(applicationContext, MapsActivity::class.java)
                .putExtra("title", title.text.toString() )
                .putExtra("message", description.text.toString())
                .putExtra("remindertime", timeText.text.toString())
                .putExtra("reminderdate",dateText.text.toString())
            )
        }

        val speechdesc = findViewById<Button>(R.id.speechdesc)
        speechdesc.setOnClickListener {
            askSpeechInputForDescription()
        }

        val extras = intent.extras

        if (extras != null && extras.getString("source") == "map") {
            lat = extras.getString("lat").toString()
            lng = extras.getString("lng").toString()
            latlang = extras.get("latlang") as LatLng

            location.setText(lat+", "+ lng)
            title.setText(extras.getString("title").toString())
            description.setText(extras.getString("message").toString())
            timeText.setText(extras.getString("remindertime").toString())
            dateText.setText(extras.getString("reminderdate").toString())

        }

        if (extras != null && extras.getString("source") == "edit") {
            isUpdate = true
            uid = extras.getInt("uid")
            title.setText(extras.getString("title"))
            description.setText(extras.getString("message"))
            location.setText(extras.getString("locationx"))
            dateText.setText(extras.getString("reminderdate"))
            timeText.setText(extras.getString("remindertime"))
            location.setText(extras.getString("locationx")+", "+ extras.getString("locationy"))
            addReminder.setText("update")
            addTaskText.setText("Update the Task")

            savedimg = extras.getString("image").toString()
            savedicon = extras.getString("icon").toString()

            if(savedicon != "") {
                if(savedicon == "star") {
                    iconimg.setImageResource(R.drawable.ic_baseline_star_rate_24)
                    selectedIcon = "star"
                } else if(savedicon == "calender") {
                    iconimg.setImageResource(R.drawable.ic_baseline_perm_contact_calendar_24)
                    selectedIcon = "calender"
                } else if(savedicon == "camera") {
                    iconimg.setImageResource(R.drawable.ic_menu_camera)
                    selectedIcon = "camera"
                } else if(savedicon == "book") {
                    iconimg.setImageResource(R.drawable.ic_baseline_book_24)
                    selectedIcon = "book"
                } else if(savedicon == "car") {
                    iconimg.setImageResource(R.drawable.ic_baseline_electric_car_24)
                    selectedIcon = "car"
                } else if(savedicon== "movie") {
                    iconimg.setImageResource(R.drawable.ic_baseline_movie_24)
                    selectedIcon = "movie"
                } else {

                }
            }
            if(savedimg == "") {
//            imageView.setImageResource(R.drawable.profile)
            } else {
                deleteimagebtn.visibility = View.VISIBLE
                val imageBytes = Base64.decode(extras.getString("image"), Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                imageView.setImageBitmap(decodedImage)
            }
        }

        popupMenu()

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        dateText.setOnClickListener {
            val dpd = DatePickerDialog(
                this@AddTaskActivity,
                R.style.DialogTheme,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    dateText.setText("" + dayOfMonth + " " + MONTHS[monthOfYear] + " " + year)
                    calyear = year
                    calmonth = monthOfYear
                    calday = dayOfMonth
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        timeText.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                timeText.setText(SimpleDateFormat("HH:mm").format(cal.time))
                hourOfDay = hour
                minuteOfHour = minute
            }
            TimePickerDialog(
                this, R.style.DialogTheme, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                    Calendar.MINUTE
                ), true
            ).show()

        }

        addImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        cancelbtn.setOnClickListener {
            startActivity(Intent(applicationContext, MenuActivity::class.java))
        }

        deleteimagebtn.setOnClickListener {
            imageView.setImageURI(null)
            imageView.setImageBitmap(null)
            deleteimagebtn.visibility = View.GONE
        }

        val request = OneTimeWorkRequest.Builder(ReminderWorker::class.java)

        addReminder.setOnClickListener {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())

            if(isUpdate) {
                var img = ""
                if(imageView.drawable != null) {
                    val bitmap = (imageView.getDrawable() as BitmapDrawable).bitmap
                    img = encodeImage(bitmap).toString()
                }

                val reminderCalenderupdate = GregorianCalendar(
                    calyear,
                    calmonth,
                    calday,
                    hourOfDay,
                    minuteOfHour,
                    0
                )
                AsyncTask.execute {
                    val db = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java,
                        getString(R.string.dbFileName)
                    ).build()
                    val loc = location.text.split(",")
                    db.reminderDao().update(
                        uid,
                        title.text.toString(),
                        description.text.toString(),
                        loc[0],
                        loc[1],
                        timeText.text.toString(),
                        dateText.text.toString(),
                        img,
                        "1",
                        selectedIcon,
                        false,
                        currentDate.toString(),
                    )
                    db.close()
                    if (reminderCalenderupdate.timeInMillis > Calendar.getInstance().timeInMillis) {
                        // set reminder
                        val message =
                                " ${title.text}  "
                        val timediff = reminderCalenderupdate.timeInMillis - Calendar.getInstance().timeInMillis

                        val data = Data.Builder()
                        data.putString("message", message)
                        request.setInputData(data.build())
                        request.setInitialDelay(timediff, TimeUnit.MILLISECONDS)

                        WorkManager.getInstance(this).enqueue(request.build())
                    }

                }
                if(reminderCalenderupdate.timeInMillis>Calendar.getInstance().timeInMillis){
                    Toast.makeText(
                        applicationContext,
                        "Reminder updated",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                var reminder = Reminder(
                    null,
                    title = title.text.toString(),
                    message = description.text.toString(),
                    locationx = lat,
                    locationy = lng,
                    remindertime = timeText.text.toString(),
                    reminderdate = dateText.text.toString(),
                    image = imagestr,
                    createrid = "1",
                    icon = selectedIcon,
                    reminderseen = false,
                    creationtime = timeText.text.toString(),
                )

                val reminderCalender = GregorianCalendar(
                    calyear,
                    calmonth,
                    calday,
                    hourOfDay,
                    minuteOfHour,
                    0
                )

                AsyncTask.execute {
                    //save reminder to room datbase
                    val db = Room.databaseBuilder(
                        applicationContext,
                        AppDatabase::class.java,
                        getString(R.string.dbFileName)
                    ).build()
                    val uuid = db.reminderDao().insert(reminder).toInt()
                    db.close()

                    // set reminder if location set but time not set
                    if(latlang != null && (reminderCalender.timeInMillis < Calendar.getInstance().timeInMillis )) {
                        createGeofence(latlang!!, uuid, geofencingClient)
                        Log.d("Location Reminder", "Location Reminder Set")
                    }

                    // set reminder when time is set but location is not set
                    if (reminderCalender.timeInMillis > Calendar.getInstance().timeInMillis && (latlang == null)) {
                        val message =
                                " ${reminder.title}  "

                        val timediff = reminderCalender.timeInMillis - Calendar.getInstance().timeInMillis

                        val data = Data.Builder()
                        data.putString("message", message)
                        request.setInputData(data.build())
                        request.setInitialDelay(timediff, TimeUnit.MILLISECONDS)

                        WorkManager.getInstance(this).enqueue(request.build())

                    }

                    if(reminderCalender.timeInMillis > Calendar.getInstance().timeInMillis && (latlang != null)) {
                        createGeofenceandTime(applicationContext, latlang!!, uuid, geofencingClient, reminderCalender.timeInMillis, reminder.title.toString())
                        Log.d("Location Reminder", "Location and Time Reminder Set")
                    }
                }

                if(reminderCalender.timeInMillis>Calendar.getInstance().timeInMillis){
                    Toast.makeText(
                        applicationContext,
                        "Time Reminder set",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            finish()
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.build().id)
            .observe(this, Observer {

                val status: String = it.state.name
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
            })


    }

    private fun createGeofenceandTime(
        context: Context,
        latlang: LatLng,
        uuid: Int,
        geofencingClient: GeofencingClient,
        timeInMillis: Long,
        message: String
    ) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            createTimeGeofence(context,latlang!!, uuid, geofencingClient,timeInMillis,
                message)
        }

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun createTimeGeofence(context: Context,
                                   location: LatLng,
                                   uuid: Int,
                                   geofencingClient: GeofencingClient,
                                   timeInMillis: Long,
                                   message: String) {
        val geofence = Geofence.Builder()
            .setRequestId(GEOFENCE_ID)
            .setCircularRegion(location.latitude,location.longitude, 500.toFloat())
            .setExpirationDuration(GEOFENCE_EXPIRATION.toLong())
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL)
            .setLoiteringDelay(GEOFENCE_DWELL_DELAY)
            .build()

        val geofenceRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        val intent = Intent(this, TimeLocationReceiver::class.java)
            .putExtra("key", uuid)
            .putExtra("message", message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            uid,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmPendingIntent =PendingIntent.getBroadcast(context,uid,intent,PendingIntent.FLAG_ONE_SHOT)

        //create a service to moniter and execute the fure action.
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if(ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    GEOFENCE_LOCATION_REQUEST_CODE
                )
            } else {
                geofencingClient.addGeofences(geofenceRequest,pendingIntent)
                alarmManager.setExact(AlarmManager.RTC, timeInMillis, alarmPendingIntent)
            }
        } else {
            geofencingClient.addGeofences(geofenceRequest,pendingIntent)
            alarmManager.setExact(AlarmManager.RTC, timeInMillis, alarmPendingIntent)
        }

    }

    private fun askSpeechInputForDescription() {
        if(!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Speech Recognition is not available", Toast.LENGTH_SHORT).show()
        } else {
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Add task description")
            startActivityForResult(i, RQ_SPEECH_DEC)
        }
    }

    private fun askSpeechInput() {
        if(!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Speech Recognition is not available", Toast.LENGTH_SHORT).show()
        } else {
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Add Task")
            startActivityForResult(i, RQ_SPEECH_REC)
        }
    }



    private fun popupMenu() {
        iconimg = findViewById(R.id.imageView2)
        val textfield = findViewById<TextView>(R.id.textView21)
        val popupmenu = PopupMenu(applicationContext, iconimg)
        popupmenu.inflate(R.menu.icon_pop_up_menu)
        popupmenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.star_icon -> {
                    iconimg.setImageResource(R.drawable.ic_baseline_star_rate_24)
                    selectedIcon = "star"
                    true
                }
                R.id.calender_icon -> {
                    iconimg.setImageResource(R.drawable.ic_baseline_perm_contact_calendar_24)
                    selectedIcon = "calender"
                    true
                }
                R.id.camera_icon -> {
                    iconimg.setImageResource(R.drawable.ic_menu_camera)
                    selectedIcon = "camera"
                    true
                }
                R.id.book_icon -> {
                    iconimg.setImageResource(R.drawable.ic_baseline_book_24)
                    selectedIcon = "book"
                    true
                }
                R.id.car_icon -> {
                    iconimg.setImageResource(R.drawable.ic_baseline_electric_car_24)
                    selectedIcon = "car"
                    true
                }
                R.id.movie_icon -> {
                    iconimg.setImageResource(R.drawable.ic_baseline_movie_24)
                    selectedIcon = "movie"
                    true
                }
                else -> true
            }
        }

        textfield.setOnClickListener {
            try {
                val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val menu = popup.get(popupmenu)
                menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java).invoke(
                    menu,
                    true
                )
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                popupmenu.show()
            }
             true

        }
    }

    private fun createGeofence(location: LatLng, key: Int, geofencingClient: GeofencingClient) {
        val geofence = Geofence.Builder()
            .setRequestId(GEOFENCE_ID)
            .setCircularRegion(location.latitude,location.longitude, 500.toFloat())
            .setExpirationDuration(GEOFENCE_EXPIRATION.toLong())
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL)
            .setLoiteringDelay(GEOFENCE_DWELL_DELAY)
            .build()

        val geofenceRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        val intent = Intent(this, ReminderReceiver::class.java)
            .putExtra("key", key)
            .putExtra("message", "inlocation")

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if(ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    GEOFENCE_LOCATION_REQUEST_CODE
                )
            } else {
                geofencingClient.addGeofences(geofenceRequest,pendingIntent)
            }
        } else {
            geofencingClient.addGeofences(geofenceRequest,pendingIntent)
        }

    }



    private fun manageImageFromUri(imageUri: Uri?) {
        var bitmap: Bitmap? = null
        var basestr =""
        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                this.contentResolver, imageUri
            )
        } catch (e: Exception) {
            // Manage exception ...
        }
        if (bitmap != null) {
            val nh = (bitmap.height * (32.0 / bitmap.width)).toInt()
            val scaled = Bitmap.createScaledBitmap(bitmap, 32, nh, true)
            basestr = encodeImage(scaled).toString()
            imagestr = basestr
        }
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            deleteimagebtn.visibility = View.VISIBLE
        }
        if (resultCode === RESULT_OK) {
            when (requestCode) {
                pickImage -> manageImageFromUri(data?.data)
            }
        } else {
            Log.d("Image Save", "Failed to save the image")
        }

        if(requestCode == RQ_SPEECH_REC && resultCode == RESULT_OK ) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            title.setText(result?.get(0).toString())
        }

        if(requestCode == RQ_SPEECH_DEC && resultCode == RESULT_OK ) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            description.setText(result?.get(0).toString())
        }
    }
}