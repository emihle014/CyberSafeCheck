package com.example.cybersafecheck.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReportDao {

    @Insert
    suspend fun insert(report: ReportEntity)

    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    suspend fun getAll(): List<ReportEntity>
}

//Author: Mangesana E
//Student number: 2030630053
