package com.example.remindbuddy

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.remindbuddy.db.Reminder
import java.util.*


class ReminderAdapter(context: Context, private val list: List<Reminder>, private val isShowAll: Boolean) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    val MONTHS = listOf<String>(
            "Jan",
            "Feb",
            "March",
            "Apr",
            "May",
            "June",
            "July",
            "Aug",
            "Sept",
            "Oct",
            "Nov",
            "Dec"
    )
    override fun getView(position: Int, convertView: View?, container: ViewGroup?): View? {

        val rowView = inflater.inflate(R.layout.reminder_item, null, true)

        val titleText = rowView.findViewById(R.id.remindertitle) as TextView
        val imageView = rowView.findViewById(R.id.reminderimg) as ImageView
        val iconimage = rowView.findViewById(R.id.iconimage) as ImageView
        val desctxt = rowView.findViewById(R.id.descriptiontxt) as TextView

        titleText.text = list[position].title
        desctxt.text = list[position].reminderdate

        // hide elements
        val reminderDate = list[position].reminderdate
        val reminderTime = list[position].remindertime

        val locationX = list[position].locationx
        val locationY = list[position].locationy

        if(reminderTime != "" && reminderDate!="" ) {
            val dateParts: List<String> = reminderDate.split(" ")
            val day = dateParts[0].toInt()
            var month = dateParts[1]
            val year = dateParts[2].toInt()
            month = MONTHS.indexOf(month).toString()
            val hourParts: List<String> = reminderTime.split(":")
            val hour = hourParts[0].toInt()
            var minute = hourParts[1].toInt()


            val reminderCalender = GregorianCalendar(year, month.toInt(), day, hour, minute, 0)
            if (reminderCalender.timeInMillis > Calendar.getInstance().timeInMillis  && !isShowAll) {
                return inflater.inflate(R.layout.blank_layout, null, false);
            }
        }

        if((!locationX.isEmpty() || !locationY.isEmpty()) &&!isShowAll) {
            return inflater.inflate(R.layout.blank_layout, null, false);
        }

        if(list[position].icon != "") {
            if(list[position].icon == "star") {
                iconimage.setImageResource(R.drawable.ic_baseline_star_rate_24)
            } else if(list[position].icon == "calender") {
                iconimage.setImageResource(R.drawable.ic_baseline_perm_contact_calendar_24)
            } else if(list[position].icon == "camera") {
                iconimage.setImageResource(R.drawable.ic_menu_camera)
            } else if(list[position].icon == "book") {
                iconimage.setImageResource(R.drawable.ic_baseline_book_24)
            } else if(list[position].icon == "car") {
                iconimage.setImageResource(R.drawable.ic_baseline_electric_car_24)
            } else if(list[position].icon == "movie") {
                iconimage.setImageResource(R.drawable.ic_baseline_movie_24)
            } else {

            }
        }

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