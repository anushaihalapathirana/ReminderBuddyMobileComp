package com.example.remindbuddy

import android.app.Activity
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory


class ReminderAdapter(private val context: Activity, private val title: Array<String>, private val imgid: Array<Int>, private val descriptions: ArrayList<String>)
    : ArrayAdapter<String>(context, R.layout.reminder_item, title){


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.reminder_item, null, true)

        val titleText = rowView.findViewById(R.id.remindertitle) as TextView
        val imageView = rowView.findViewById(R.id.reminderimg) as ImageView
        val radiobtn = rowView.findViewById(R.id.radioButton) as CheckBox
        val desctxt = rowView.findViewById(R.id.descriptiontxt) as TextView

        titleText.text = title[position]
        desctxt.text = descriptions[position]
        imageView.setImageResource(imgid[position])
        val bitmap = (imageView.getDrawable() as BitmapDrawable).bitmap
        imageView.setImageDrawable(roundedBitmap(bitmap))

        return rowView
    }

    fun roundedBitmap(bitmap: Bitmap): RoundedBitmapDrawable {
        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(Resources.getSystem(), bitmap)
        roundedBitmapDrawable.isCircular = true

//        roundedBitmapDrawable.cornerRadius = Math.max(bitmap.width, bitmap.height) / 5.0f
        return roundedBitmapDrawable

    }

}