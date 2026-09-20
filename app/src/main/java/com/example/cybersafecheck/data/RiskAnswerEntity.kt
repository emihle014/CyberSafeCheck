package com.example.cybersafecheck.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "risk_answers")
data class RiskAnswerEntity(
    @PrimaryKey val itemId: String,
    val question: String,
    val category: String,
    val isFlagged: Boolean
)