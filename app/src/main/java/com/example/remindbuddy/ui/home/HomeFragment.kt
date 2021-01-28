package com.example.remindbuddy.ui.home

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.remindbuddy.R
import com.example.remindbuddy.ReminderAdapter

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
        val language = arrayOf<String>("C","C++","Java",".Net","Kotlin","Ruby","Rails","Python","Java Script","Php","Ajax","Perl","Hadoop")
        val imageId = arrayOf<Int>(
            R.drawable.backspace,R.drawable.backspace,R.drawable.backspace,
            R.drawable.backspace,R.drawable.backspace,R.drawable.backspace,
            R.drawable.backspace,R.drawable.backspace,R.drawable.backspace,
            R.drawable.backspace,R.drawable.backspace,R.drawable.backspace,
            R.drawable.backspace
        )

        val myListAdapter = ReminderAdapter(context as Activity,language,imageId)
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