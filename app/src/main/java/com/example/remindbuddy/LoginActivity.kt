package com.example.remindbuddy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val registertxt = findViewById<TextView>(R.id.registertxt)
        var iskeeplogin = false
        val loginwithpinbtn = findViewById<Button>(R.id.loginbtninactivity2)

        findViewById<Button>(R.id.loginbtninactivity).setOnClickListener {
            Log.d("Login", "User Logged in")
            val username = findViewById<EditText>(R.id.editTextTextPersonName).text
            val password = findViewById<EditText>(R.id.editTextTextPassword).text
            val errorMessage = findViewById<TextView>(R.id.errorbanner)
            val checkBox = findViewById<CheckBox>(R.id.checkBox)

            Log.d("Password", password.toString())
            Log.d("Username", username.toString())

            val savedusername = applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).getString("username", "janedoe")
            val savedpassword = applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).getString("password", "MobileComp")

            if(username.toString() == savedusername && password.toString() == savedpassword) {
                errorMessage.setText("")
                applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).edit().putInt("LoginStatus", 1).apply()

                startActivity(Intent(applicationContext, MenuActivity::class.java))
            } else {
                errorMessage.setText("Username or Password is Incorrect.")
            }

        }

        loginwithpinbtn.setOnClickListener {
            startActivity(Intent(applicationContext, PincodeActivity::class.java))
        }

        registertxt.setOnClickListener {
            startActivity(Intent(applicationContext, SignupActivity::class.java))
        }
    }
}