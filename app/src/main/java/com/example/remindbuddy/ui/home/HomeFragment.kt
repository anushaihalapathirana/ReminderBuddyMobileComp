package com.example.remindbuddy.ui.home

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.remindbuddy.AddTaskActivity
import com.example.remindbuddy.R
import com.example.remindbuddy.ReminderAdapter
import com.example.remindbuddy.db.AppDatabase
import com.example.remindbuddy.db.Reminder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        listView = root.findViewById(R.id.reminder_list)

        refreshListView()

        // floating button click
        val fab: FloatingActionButton = root.findViewById(R.id.editbtn)
        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
            startActivity(Intent(activity, AddTaskActivity::class.java))
        }


        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, id ->
            //retrieve selected Item
            val selectedReminder = listView.adapter.getItem(position) as Reminder
            val message =
                "Do you want to delete ?"

            // Show AlertDialog to delete the reminder
            val builder = AlertDialog.Builder(context as Activity)
            builder.setTitle("Delete reminder?")
                .setMessage(message)
                .setPositiveButton("Delete") { _, _ ->
                    // Update UI


                    //delete from database
                    AsyncTask.execute {
                        val db = Room
                            .databaseBuilder(
                                context as Activity,
                                AppDatabase::class.java,
                                getString(R.string.dbFileName)
                            )
                            .build()
                        db.paymentDao().delete(selectedReminder.uid!!)
                    }
                    //refresh payments list
                    refreshListView()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    // Do nothing
                    dialog.dismiss()
                }
                .show()

        }
        return root
    }

    override fun onResume() {
        super.onResume()
        refreshListView()
    }

    private fun refreshListView() {
        var refreshTask = LoadPaymentInfoEntries()
        refreshTask.execute()
    }

    inner class LoadPaymentInfoEntries : AsyncTask<String?, String?, List<Reminder>>() {
        override fun doInBackground(vararg params: String?): List<Reminder> {
            val db = Room.databaseBuilder( context as Activity, AppDatabase::class.java, getString(R.string.dbFileName))
                .build()
            val paymentInfos = db.paymentDao().getPaymentInfos()
            db.close()
            return paymentInfos
        }

        override fun onPostExecute(paymentInfos: List<Reminder>?) {
            super.onPostExecute(paymentInfos)
            if (paymentInfos != null) {
                if (paymentInfos.isNotEmpty()) {
                    val adaptor = ReminderAdapter(context as Activity, paymentInfos)
                    listView.adapter = adaptor
                } else {
                    listView.adapter = null
                    Toast.makeText(context as Activity, "No tasks available", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


}