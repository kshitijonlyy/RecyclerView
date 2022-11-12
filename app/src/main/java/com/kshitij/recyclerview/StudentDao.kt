package com.kshitij.recyclerview

import androidx.room.*

@Dao
interface StudentDao {
    @Insert
    fun insertStudent(student: StudentTable)

    @Query("SELECT * FROM StudentTable")
    fun getStudent():List<StudentTable>

    @Update
    fun editStudent(student: StudentTable)

    @Delete
    fun deleteStudent(student: StudentTable)
}