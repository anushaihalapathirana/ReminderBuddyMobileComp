package com.example.remindbuddy.ui.gallery

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.remindbuddy.EditProfileActivity
import com.example.remindbuddy.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class GalleryFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_gallery, container, false)

        val editbtn: FloatingActionButton = root.findViewById(R.id.editbtn)
        editbtn.setOnClickListener {
           startActivity(Intent(activity, EditProfileActivity::class.java))
        }

        val name = root.findViewById<TextView>(R.id.profnametxtg)
        val usernametxt = root.findViewById<TextView>(R.id.usernametxtg)
        val emailtxt = root.findViewById<TextView>(R.id.emailtxtg)
        val passwordtxt = root.findViewById<TextView>(R.id.passtxtg)
        val image = root.findViewById<ImageView>(R.id.editprofimage)
        val pin = root.findViewById<TextView>(R.id.passtxtg2)

        val prefs = activity?.getSharedPreferences(
            getString(R.string.sharedPreference),
            MODE_PRIVATE
        )
        val savedname = prefs!!.getString("name", "John Doe")
        val savedemail =prefs!!.getString("email","janedoe@gmail.com")
        val savedusername = prefs!!.getString("username","janedoe")
        val savedpassword = prefs!!.getString("password","MobileComp")
        val photobase = prefs!!.getString("photo", "")
        val savedpin = prefs!!.getString("pin", "1111")

        if(photobase == "") {
            Log.d("Photo", "No profile picture saved")
            image.setImageResource(R.drawable.profile)
        } else {
            Log.d("Photo", photobase.toString())
            val imageBytes = Base64.decode(photobase, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            image.setImageBitmap(decodedImage)
        }

        name.setText(savedname.toString())
        usernametxt.setText(savedusername.toString())
        emailtxt.setText(savedemail.toString())
        passwordtxt.setText(savedpassword.toString())
        pin.setText(savedpin.toString())
        return root
    }
}