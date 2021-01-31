package com.example.remindbuddy

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.remindbuddy.ui.gallery.GalleryFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.*


class EditProfileActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var floatingbtn: FloatingActionButton
    private val pickImage = 100
    private var imageUri: Uri? = null
    private val myContext: GalleryFragment? = null
    val IMAGEREQUESTCODE = 8242008

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        imageView = findViewById(R.id.editprofimage)
        floatingbtn = findViewById(R.id.editbtn)

        val name = findViewById<EditText>(R.id.profnametxtg)
        val usernametxt = findViewById<EditText>(R.id.usernametxtg)
        val emailtxt = findViewById<EditText>(R.id.emailtxtg)
        val passwordtxt = findViewById<EditText>(R.id.passtxtg)
        val conpasstxt = findViewById<EditText>(R.id.conpasstxt)
        val err = findViewById<TextView>(R.id.errmsg)
        val savebtn = findViewById<Button>(R.id.savebtn)

        val savedname = applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).getString("name", "Jane Doe")
        val savedemail = applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).getString("email", "janedoe@gmail.com")
        val savedusername = applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).getString("username", "johndoe")
        val savedpassword = applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).getString("password", "123456")
        val savedppicture = applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).getString("photo", "")

        if(savedppicture == "") {
            imageView.setImageResource(R.drawable.profile)
        } else {
            val imageBytes = Base64.decode(savedppicture, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            imageView.setImageBitmap(decodedImage)
        }

        name.setText(savedname.toString())
        usernametxt.setText(savedusername.toString())
        emailtxt.setText(savedemail.toString())
        passwordtxt.setText(savedpassword.toString())
        conpasstxt.setText(savedpassword.toString())



        savebtn.setOnClickListener {
            if(isValidPass(passwordtxt.text.toString(), conpasstxt.text.toString())) {
                Log.d("Edit profile", "Save successfully")
                applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).edit().putString("name", name.text.toString()).apply()
                applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).edit().putString("username", usernametxt.text.toString()).apply()
                applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).edit().putString("email", emailtxt.text.toString()).apply()
                applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).edit().putString("password", passwordtxt.text.toString()).apply()
                startActivity(Intent(applicationContext, MenuActivity::class.java))
            } else {
                Log.d("Edit profile", "Save failed")
                err.setText("Password not matching")
            }
        }

        floatingbtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)


        }
    }

    private fun isValidPass(pass: String, confirm: String): Boolean {
        return pass.equals(confirm)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
        }

        if (resultCode === RESULT_OK) {
            when (requestCode) {
                pickImage -> manageImageFromUri(data?.data)
            }
        } else {
            Log.d("Image Save", "Failed to save the image")
        }
    }

    private fun manageImageFromUri(imageUri: Uri?) {
        var bitmap: Bitmap? = null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(
                    this.contentResolver, imageUri)
        } catch (e: Exception) {
            // Manage exception ...
        }
        if (bitmap != null) {
            var basestr = encodeImage(bitmap)
            if (basestr != null) {
                applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).edit().putString("photo", basestr).apply()
            }
        }
    }
    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

}