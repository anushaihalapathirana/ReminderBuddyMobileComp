package com.example.remindbuddy.db
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ReminderDao {
    @Transaction
    @Insert
    fun insert(reminder: Reminder): Long

    @Query("DELETE FROM reminder WHERE uid = :id")
    fun delete(id: Int)

    @Query("SELECT * FROM reminder")
    fun getPaymentInfos(): List<Reminder>

}

