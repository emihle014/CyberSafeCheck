package com.example.cybersafecheck.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val description: String,
    val photoUri: String?
)

//Author: Mangesana E
//Student number: 2030630053
