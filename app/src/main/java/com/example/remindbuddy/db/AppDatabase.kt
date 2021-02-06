package com.example.remindbuddy.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Reminder::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao
}
