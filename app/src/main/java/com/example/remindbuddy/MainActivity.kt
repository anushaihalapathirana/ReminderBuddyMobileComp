package com.example.remindbuddy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.loginbtn).setOnClickListener {
            Log.d("Login", "Click login")
            applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).edit().putInt("LoginStatus", 1).apply()
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }
        // check login status
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        val loginStatus = applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).getInt("LoginStatus",0)
        if(loginStatus == 1) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }
    }
}