package com.example.remindbuddy

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.remindbuddy.db.Reminder

class ReminderAdapter(context: Context, private val list: List<Reminder>) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, container: ViewGroup?): View? {

        val rowView = inflater.inflate(R.layout.reminder_item, null, true)

        val titleText = rowView.findViewById(R.id.remindertitle) as TextView
        val imageView = rowView.findViewById(R.id.reminderimg) as ImageView
        val radiobtn = rowView.findViewById(R.id.radioButton) as CheckBox
        val desctxt = rowView.findViewById(R.id.descriptiontxt) as TextView

        titleText.text = list[position].title
        desctxt.text = list[position].reminderdate

        if(list[position].image == "") {
//            imageView.setImageResource(R.drawable.profile)
        } else {
            val imageBytes = Base64.decode(list[position].image, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            imageView.setImageDrawable(roundedBitmap(decodedImage))
        }
        return rowView
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    fun roundedBitmap(bitmap: Bitmap): RoundedBitmapDrawable {
        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(Resources.getSystem(), bitmap)
        roundedBitmapDrawable.isCircular = true

        return roundedBitmapDrawable

    }

}