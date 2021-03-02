package com.example.remindbuddy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.remindbuddy.ui.home.HomeFragment
import java.util.*
import java.util.Calendar.*

class TimeLocationReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        // Retrieve data from intent
        val uid = intent?.getIntExtra("uid", 0)
        val text = intent?.getStringExtra("message")
        val date = intent?.getStringExtra("date")

        if (date != null) {
            if((date.toLong() < (getInstance().timeInMillis + 10000)) && (date.toLong() > (getInstance().timeInMillis - 10000))) {
                HomeFragment.showNotification(context!!,text!!)
            } else {

            }
        }
    }
}