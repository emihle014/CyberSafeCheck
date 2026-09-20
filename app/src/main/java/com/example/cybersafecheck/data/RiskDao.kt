package com.example.cybersafecheck.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RiskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RiskAnswerEntity>)

    @Query("SELECT * FROM risk_answers")
    suspend fun getAll(): List<RiskAnswerEntity>

    @Query("SELECT * FROM risk_answers WHERE itemId = :id")
    suspend fun getById(id: String): RiskAnswerEntity?

    @Query("UPDATE risk_answers SET isFlagged = :flagged WHERE itemId = :id")
    suspend fun updateFlagged(id: String, flagged: Boolean)

    @Query("SELECT COUNT(*) FROM risk_answers")
    suspend fun count(): Int
}