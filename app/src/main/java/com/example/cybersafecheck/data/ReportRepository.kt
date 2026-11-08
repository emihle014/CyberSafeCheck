package com.example.cybersafecheck.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReportRepository(private val dao: ReportDao) {

    suspend fun saveReport(description: String, photoUri: String?) = withContext(Dispatchers.IO) {
        dao.insert(
            ReportEntity(
                timestamp = System.currentTimeMillis(),
                description = description,
                photoUri = photoUri
            )
        )
    }

    suspend fun getAll(): List<ReportEntity> = withContext(Dispatchers.IO) {
        dao.getAll()
    }
}

//Author: Mangesana E
//Student number: 2030630053
