package com.example.remindbuddy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.remindbuddy.ui.home.HomeFragment

class TimeLocationReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        // Retrieve data from intent
        val uid = intent?.getIntExtra("uid", 0)
        val text = intent?.getStringExtra("message")

        HomeFragment.showNotification(context!!,text!!)
    }
}