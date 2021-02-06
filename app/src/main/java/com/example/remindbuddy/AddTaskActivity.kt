package com.example.remindbuddy

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    private val pickImage = 100
    private var imageUri: Uri? = null
    val MONTHS = listOf<String>("Jan", "Feb", "March", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec")
    lateinit var imageView: ImageView
    lateinit var addImage: TextView
    lateinit var deleteimagebtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val dateText = findViewById<EditText>(R.id.editTextDate)
        val timeText = findViewById<EditText>(R.id.editTextTime)
        val cancelbtn = findViewById<Button>(R.id.addtaskbtn2)
        imageView = findViewById(R.id.imageView)
        addImage = findViewById(R.id.addimagetxt)
        deleteimagebtn = findViewById(R.id.deletebtn)
        deleteimagebtn.visibility = View.GONE

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        dateText.setOnClickListener {
            val dpd = DatePickerDialog(this@AddTaskActivity, R.style.DialogTheme, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                dateText.setText("" + dayOfMonth + " " + MONTHS[monthOfYear] + " " + year)
            }, year, month, day)
            dpd.show()
        }

        timeText.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                timeText.setText(SimpleDateFormat("HH:mm").format(cal.time))
            }
            TimePickerDialog(this, R.style.DialogTheme, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()

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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            deleteimagebtn.visibility = View.VISIBLE
        }
    }
}