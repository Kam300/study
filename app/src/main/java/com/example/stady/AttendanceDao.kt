package com.example.stady

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AttendanceDao {
    @Insert
    suspend fun insertAttendance(attendance: Attendance)

    @Query("SELECT * FROM attendance WHERE  classNumber = :classNumber")
    suspend fun getAttendance( classNumber: Int): List<Attendance>

    @Query("SELECT DISTINCT `group` FROM students")
    suspend fun getAllGroups(): List<String>

    @Query("SELECT DISTINCT classNumber FROM attendance")
    suspend fun getAllClassNumbers(): List<Int>

}
