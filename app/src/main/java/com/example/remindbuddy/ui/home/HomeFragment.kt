package com.example.remindbuddy.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.remindbuddy.AddTaskActivity
import com.example.remindbuddy.EditProfileActivity
import com.example.remindbuddy.R
import com.example.remindbuddy.ReminderAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        var listView = root.findViewById<ListView>(R.id.reminder_list)
        val language = arrayOf<String>("Shopping","Call Sam","Assignment 1 due","Design a prototype","Follow Tutorial","Lab assignment",
                "Lecture 2 attend","Add Styles","Dinner out","Plan vacation","Add icons to the application","Follow up kotlin tutorial","Movie night")
        val imageId = arrayOf<Int>(
            R.drawable.itemimg,R.drawable.i7,R.drawable.i8,
            R.drawable.i1,R.drawable.i2,R.drawable.i3,
            R.drawable.i3,R.drawable.i8,R.drawable.i4,
            R.drawable.i2,R.drawable.i6,R.drawable.i5,
            R.drawable.i1
        )

        // floating button click
        val fab: FloatingActionButton = root.findViewById(R.id.editbtn)
        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
            startActivity(Intent(activity, AddTaskActivity::class.java))
        }



        val desc = arrayListOf<String>("Wed, Feb 22, 10:00 AM",
        "Mon, Feb 25, 08:00 AM",
        "Wed, Feb 26, 01:00 PM",
        "Sat, Feb 27, 10:00 AM",
        "Sun, Feb 28, 08:00 AM",
        "Mon, March 1, 07:00 AM",
        "Wed, March 3, 10:00 AM",
        "Thu, March 20, 10:00 AM",
        "Fri, March 22, 09:00 AM",
        "Wed, March 25, 10:00 AM",
        "Mon, March 28, 11:00 AM",
        "Sun, March 30, 02:00 PM",
        "Mon, March 31, 05:00 PM")

        val myListAdapter = ReminderAdapter(context as Activity,language,imageId, desc)
        listView.adapter = myListAdapter

        listView.setOnItemClickListener(){
                adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
            Log.d("List item click", "Click on item at $itemAtPos its item id $itemIdAtPos")
        }

        return root
    }
}