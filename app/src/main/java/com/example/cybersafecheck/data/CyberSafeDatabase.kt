package com.example.cybersafecheck.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [RiskAnswerEntity::class, AssessmentEntity::class, ReportEntity::class],
    version = 2,
    exportSchema = false
)
abstract class CyberSafeDatabase : RoomDatabase() {

    abstract fun riskDao(): RiskDao
    abstract fun assessmentDao(): AssessmentDao
    abstract fun reportDao(): ReportDao

    companion object {
        @Volatile
        private var INSTANCE: CyberSafeDatabase? = null

        // Version 1 -> 2: adds the reports table without wiping existing answers/history
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `reports` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`timestamp` INTEGER NOT NULL, " +
                            "`description` TEXT NOT NULL, " +
                            "`photoUri` TEXT)"
                )
            }
        }

        fun getInstance(context: Context): CyberSafeDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CyberSafeDatabase::class.java,
                    "cybersafe.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build().also { INSTANCE = it }
            }
        }
    }
}

//Author: Mangesana E
//Student number: 2030630053
