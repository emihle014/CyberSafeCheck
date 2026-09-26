package com.example.cybersafecheck.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assessments")
data class AssessmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val flaggedCount: Int,
    val totalCount: Int
)


//Author: Mangesana E
//Student number: 2030630053