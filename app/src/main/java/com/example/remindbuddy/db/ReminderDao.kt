package com.example.remindbuddy.db
import androidx.room.*

@Dao
interface ReminderDao {
    @Transaction
    @Insert
    fun insert(reminder: Reminder): Long

    @Query("DELETE FROM reminder WHERE uid = :id")
    fun delete(id: Int)

    @Query("SELECT * FROM reminder")
    fun getReminderInfo(): List<Reminder>

    @Query("UPDATE reminder SET title = :title, message = :message, locationx=:locationx, locationy=:locationy, remindertime=:remindertime, reminderdate=:reminderdate, image=:image, createrid=:createrid WHERE uid=:uid")
    fun update(uid: Int, title: String, message: String, locationx: String, locationy: String, remindertime:String, reminderdate:String, image:String, createrid: String)
}

