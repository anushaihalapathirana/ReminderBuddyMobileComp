package com.example.remindbuddy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val name = findViewById<EditText>(R.id.editTextTextPersonName2)
        val username = findViewById<EditText>(R.id.editTextTextPersonName3)
        val email = findViewById<EditText>(R.id.editTextTextPersonName4)
        val password = findViewById<EditText>(R.id.editTextTextPassword6)
        val confirmpass = findViewById<EditText>(R.id.editTextTextPassword7)
        val pin = findViewById<EditText>(R.id.editTextNumberPassword6)

        val nameerr = findViewById<TextView>(R.id.textView4)
        val usernameerr = findViewById<TextView>(R.id.textView5)
        val emailerr = findViewById<TextView>(R.id.textView6)
        val passworderr = findViewById<TextView>(R.id.textView7)
        val confpasserr = findViewById<TextView>(R.id.textView8)
        val pinerr = findViewById<TextView>(R.id.textView12)

        name.setOnClickListener {
            nameerr.setText("")
        }
        username.setOnClickListener {
            usernameerr.setText("")
        }
        email.setOnClickListener {
            emailerr.setText("")
        }
        password.setOnClickListener {
            passworderr.setText("")
        }
        confirmpass.setOnClickListener {
            confpasserr.setText("")
        }
        pin.setOnClickListener {
            pinerr.setText("")
        }

        findViewById<Button>(R.id.addtaskbtn).setOnClickListener {
            if(name.text.toString().trim().length>0 && username.text.toString().trim().length>0 && isValidEmail(email.text.toString())
                && comparePasswords(password.text.toString(), confirmpass.text.toString()) && pin.text.toString().length == 4 ) {
                nameerr.setText("")
                usernameerr.setText("")
                emailerr.setText("")
                passworderr.setText("")
                confpasserr.setText("")
                pinerr.setText("")
                Log.d("Sign In", "Account successfully created")
                applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).edit().putString("name", name.text.toString()).apply()
                applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).edit().putString("username", username.text.toString()).apply()
                applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).edit().putString("email", email.text.toString()).apply()
                applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).edit().putString("password", password.text.toString()).apply()
                applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).edit().putString("pin", pin.text.toString()).apply()
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            } else {
                if(name.text.toString().trim().length==0) {
                    nameerr.setText("Please enter your name")
                }
                if(username.text.toString().trim().length==0) {
                    usernameerr.setText("Please enter a username")
                }
                if(!isValidEmail(email.text.toString())) {
                    Log.d("Sign In", "email error")
                    emailerr.setText("Please enter a valid Email address")
                }
                if(!comparePasswords(password.text.toString(), confirmpass.text.toString())) {
                    confirmpass.setText("")
                    password.setText("")
                    confpasserr.setText("Password not matching")
                }
                if(pin.text.toString().trim().length < 4) {
                    pinerr.setText("Please enter 4 digits as a PIN")
                }
                else {
                    Log.d("Sign In", "error")
                }

            }

        }
    }

    private fun isValidEmail(target: String): Boolean {
        return !TextUtils.isEmpty(target) || Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun comparePasswords(pass: String , confirm: String): Boolean {
        return pass.equals(confirm)
    }
}