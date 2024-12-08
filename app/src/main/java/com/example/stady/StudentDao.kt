package com.example.stady

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Insert
    suspend fun insertStudent(student: Student)

    @Query("SELECT * FROM students")
    fun getAllStudents(): Flow<List<Student>>

    @Delete
    suspend fun deleteStudent(student: Student) // Метод для удаления студента

    @Update
    suspend fun updateStudent(student: Student) // Метод для обновления студента


}


