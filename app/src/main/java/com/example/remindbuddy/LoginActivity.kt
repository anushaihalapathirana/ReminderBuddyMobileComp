package com.example.remindbuddy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val registertxt = findViewById<TextView>(R.id.registertxt)

        findViewById<Button>(R.id.loginbtninactivity).setOnClickListener {
            Log.d("Login", "User Logged in")
            val username = findViewById<TextView>(R.id.editTextTextPersonName).text
            val password = findViewById<TextView>(R.id.editTextTextPassword).text
            val errorMessage = findViewById<TextView>(R.id.errorbanner);

            Log.d("Password", password.toString())
            Log.d("Username", username.toString())

            if(username.toString() == "A" && password.toString() == "A") {
                errorMessage.setText("")
                applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).edit().putInt("LoginStatus", 1).apply()
                startActivity(Intent(applicationContext, MenuActivity::class.java))
            } else {
                errorMessage.setText("Username or Password is Incorrect.")
            }
        }

        registertxt.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
    }
}