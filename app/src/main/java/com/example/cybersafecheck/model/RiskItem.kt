package com.example.cybersafecheck.model

data class RiskItem(
    val id: String,
    val question: String,
    val category: RiskCategory,
    val explanation: String,
    var isFlagged: Boolean = false
)