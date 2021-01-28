package com.example.remindbuddy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class PincodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pincode)

        val btn1 = findViewById<Button>(R.id.imageButton1)
        val btn2 = findViewById<Button>(R.id.imageButton2)
        val btn3 = findViewById<Button>(R.id.imageButton3)
        val btn4 = findViewById<Button>(R.id.imageButton4)
        val btn5 = findViewById<Button>(R.id.imageButton5)
        val btn6 = findViewById<Button>(R.id.imageButton6)
        val btn7 = findViewById<Button>(R.id.imageButton7)
        val btn8 = findViewById<Button>(R.id.imageButton8)
        val btn9 = findViewById<Button>(R.id.imageButton9)
        val btn0 = findViewById<Button>(R.id.imageButton0)
        val backspacebtn = findViewById<ImageButton>(R.id.backspacebtn)

        val txt1 = findViewById<TextView>(R.id.editTextTextPassword2)
        val txt2 = findViewById<TextView>(R.id.editTextTextPassword3)
        val txt3 = findViewById<TextView>(R.id.editTextTextPassword4)
        val txt4 = findViewById<TextView>(R.id.editTextTextPassword5)

        txt1.text = ""
        txt2.text = ""
        txt3.text = ""
        txt4.text = ""


        btn1.setOnClickListener {
            setUpText(txt1, txt2,txt3, txt4, "1")
        }

        btn2.setOnClickListener {
            setUpText(txt1, txt2,txt3, txt4, "2")
        }

        btn3.setOnClickListener {
            setUpText(txt1, txt2,txt3, txt4, "3")
        }

        btn4.setOnClickListener {
            setUpText(txt1, txt2,txt3, txt4, "4")
        }

        btn5.setOnClickListener {
            setUpText(txt1, txt2,txt3, txt4, "5")
        }

        btn6.setOnClickListener {
            setUpText(txt1, txt2,txt3, txt4, "6")
        }

        btn7.setOnClickListener {
            setUpText(txt1, txt2,txt3, txt4, "7")
        }

        btn8.setOnClickListener {
            setUpText(txt1, txt2,txt3, txt4, "8")
        }

        btn9.setOnClickListener {
            setUpText(txt1, txt2,txt3, txt4, "9")
        }

        btn0.setOnClickListener {
            setUpText(txt1, txt2,txt3, txt4, "0")
        }

        backspacebtn.setOnClickListener {
            clearInput(txt1, txt2,txt3, txt4)
        }

    }

    private fun clearInput(txt1: TextView, txt2: TextView, txt3: TextView, txt4: TextView) {
        if (txt4.text.toString().trim().length>0) {
            txt4.text = ""
        } else if (txt3.text.toString().trim().length>0) {
            txt3.text = ""
        } else if (txt2.text.toString().trim().length>0) {
            txt2.text = ""
        } else if (txt1.text.toString().trim().length>0) {
            txt1.text = ""
        }else {
        }
    }

    private fun setUpText(txt1: TextView, txt2: TextView, txt3: TextView, txt4: TextView, number: String) {
        val errTxt = findViewById<TextView>(R.id.errtxt)
        if(txt1.text.toString() == "") {
            txt1.text = number
            errTxt.setText("")
        } else if (txt2.text.toString() == "") {
            txt2.text = number
        } else if (txt3.text.toString() == "") {
            txt3.text = number
        } else if(txt4.text.toString() == ""){
            txt4.text = number
            navigateToMenue(txt1, txt2, txt3, txt4)
        } else {

        }
    }

    private fun navigateToMenue(txt1: TextView, txt2: TextView, txt3: TextView, txt4: TextView) {
        val errTxt = findViewById<TextView>(R.id.errtxt)
        if (txt1.text.toString() == "1" && txt2.text.toString() == "1" && txt3.text.toString() == "1" && txt4.text.toString() == "1" ) {
            errTxt.setText("")
            Handler().postDelayed({
                applicationContext.getSharedPreferences(getString(R.string.sharedPreference), Context.MODE_PRIVATE).edit().putInt("LoginStatus", 1).apply()
                startActivity(Intent(applicationContext, MenuActivity::class.java))
            }, 200)

        } else {
            Log.d("Pin", "Wrong Pin")
            errTxt.setText("Incorrect PIN number. Try again")
            Handler().postDelayed({
                txt1.text = ""
                txt2.text = ""
                txt3.text = ""
                txt4.text = ""
            }, 200)
        }
    }

}