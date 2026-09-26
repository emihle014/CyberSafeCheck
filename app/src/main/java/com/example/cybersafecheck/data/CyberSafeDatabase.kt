package com.example.cybersafecheck.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RiskAnswerEntity::class, AssessmentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CyberSafeDatabase : RoomDatabase() {

    abstract fun riskDao(): RiskDao
    abstract fun assessmentDao(): AssessmentDao

    companion object {
        @Volatile
        private var INSTANCE: CyberSafeDatabase? = null

        fun getInstance(context: Context): CyberSafeDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CyberSafeDatabase::class.java,
                    "cybersafe.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

//Author: Mangesana E
//Student number: 2030630053