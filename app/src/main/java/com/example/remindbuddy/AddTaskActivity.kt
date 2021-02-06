package com.example.remindbuddy

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.remindbuddy.db.AppDatabase
import com.example.remindbuddy.db.Reminder
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    private val pickImage = 100
    private var imageUri: Uri? = null
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
    lateinit var deleteimagebtn: Button
    lateinit var imagestr: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val title = findViewById<EditText>(R.id.editTextTextPersonName5)
        var description = findViewById<EditText>(R.id.editTextTextMultiLine)
        val location = findViewById<EditText>(R.id.editTextTextPersonName6)

        val dateText = findViewById<EditText>(R.id.editTextDate)
        val timeText = findViewById<EditText>(R.id.editTextTime)
        val cancelbtn = findViewById<Button>(R.id.addtaskbtn2)
        val addReminder = findViewById<Button>(R.id.addtaskbtn)

        imageView = findViewById(R.id.imageView)
        addImage = findViewById(R.id.addimagetxt)
        deleteimagebtn = findViewById(R.id.deletebtn)
        deleteimagebtn.visibility = View.GONE

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
            deleteimagebtn.visibility = View.GONE
        }

        addReminder.setOnClickListener {
            var reminder = Reminder(
                null,
                title = title.text.toString(),
                message = description.text.toString(),
                locationx = location.text.toString(),
                locationy = location.text.toString(),
                remindertime = timeText.text.toString(),
                reminderdate = dateText.text.toString(),
                image = imagestr,
            )

            AsyncTask.execute {
                //save payment to room datbase
                val db = Room.databaseBuilder(
                    applicationContext,
                    AppDatabase::class.java,
                    getString(R.string.dbFileName)
                ).build()
                val uuid = db.paymentDao().insert(reminder).toInt()
                db.close()

            }

            finish()

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
            val nh = (bitmap.height * (128.0 / bitmap.width)).toInt()
            val scaled = Bitmap.createScaledBitmap(bitmap, 128, nh, true)
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
    }
}