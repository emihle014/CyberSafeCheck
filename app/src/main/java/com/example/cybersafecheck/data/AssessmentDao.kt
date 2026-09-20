package com.example.cybersafecheck.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AssessmentDao {

    @Insert
    suspend fun insert(assessment: AssessmentEntity)

    @Query("SELECT * FROM assessments ORDER BY timestamp DESC")
    suspend fun getAll(): List<AssessmentEntity>
}