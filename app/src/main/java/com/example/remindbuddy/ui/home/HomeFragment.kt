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



        val desc = arrayListOf<String>("Shopping with Anna and shan", "call around 9.30pm",
        "machine vision first assignment. followup with group members",
        "need new ideas for mobile application",
        "tute should be completed before noon",
        "lab 1",
        "start on 10.00am",
        "css guidlines check",
        "arround 7.30 at pastamania",
        "booking.com and agoda",
        "green and blue icons",
        "continue with tutorial 5",
        "Watch end game")
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