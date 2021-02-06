package com.example.remindbuddy.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class Reminder(
    @PrimaryKey(autoGenerate = true) var uid: Int?,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "message") var message: String,
    @ColumnInfo(name = "locationx") var locationx: String,
    @ColumnInfo(name = "locationy") var locationy: String,
    @ColumnInfo(name = "remindertime") var remindertime: String,
    @ColumnInfo(name = "reminderdate") var reminderdate: String,
    @ColumnInfo(name = "image") var image: String,
    )


