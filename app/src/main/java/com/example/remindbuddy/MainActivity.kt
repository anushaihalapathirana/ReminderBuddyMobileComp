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
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }

        findViewById<Button>(R.id.pinbtnlogin).setOnClickListener {
            Log.d("Login", "Pin Code")
            startActivity(Intent(applicationContext, PincodeActivity::class.java))
        }

        findViewById<Button>(R.id.signupbtn).setOnClickListener {
            Log.d("Login", "SignUp")
            startActivity(Intent(applicationContext, SignupActivity::class.java))
        }
        // check login status
        val loginStatus = applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).getInt("LoginStatus",0)
        Log.d("Login status", loginStatus.toString())
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        val loginStatus = applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).getInt("LoginStatus",0)
        Log.d("Login status", loginStatus.toString())
        if(loginStatus == 1) {
            startActivity(Intent(applicationContext, MenuActivity::class.java))
        } else {
            Log.d("Login status else ", loginStatus.toString())
        }
    }
}