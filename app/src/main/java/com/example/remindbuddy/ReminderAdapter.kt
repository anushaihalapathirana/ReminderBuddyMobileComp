package com.example.remindbuddy

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.remindbuddy.ui.home.HomeFragment

class ReminderAdapter(private val context: Activity, private val title: Array<String>, private val imgid: Array<Int>)
    : ArrayAdapter<String>(context, R.layout.reminder_item, title){


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //4
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.reminder_item, null, true)

        val titleText = rowView.findViewById(R.id.remindertitle) as TextView
        val imageView = rowView.findViewById(R.id.reminderimg) as ImageView


        titleText.text = title[position]
        imageView.setImageResource(imgid[position])
        return rowView
    }
}