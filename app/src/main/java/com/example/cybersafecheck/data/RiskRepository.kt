package com.example.cybersafecheck.data

import com.example.cybersafecheck.model.RiskLab
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RiskRepository(private val dao: RiskDao) {

    // Fills the table from RiskLab's list the first time the app runs
    suspend fun seedIfEmpty() = withContext(Dispatchers.IO) {
        if (dao.count() == 0) {
            dao.insertAll(
                RiskLab.items.map {
                    RiskAnswerEntity(
                        itemId = it.id,
                        question = it.question,
                        category = it.category.name,
                        isFlagged = false
                    )
                }
            )
        }
    }

    // Returned in the same order as RiskLab's list
    suspend fun getAll(): List<RiskAnswerEntity> = withContext(Dispatchers.IO) {
        val order = RiskLab.items.map { it.id }
        dao.getAll().sortedBy { order.indexOf(it.itemId) }
    }

    suspend fun getById(id: String): RiskAnswerEntity? = withContext(Dispatchers.IO) {
        dao.getById(id)
    }

    suspend fun setFlagged(itemId: String, flagged: Boolean) = withContext(Dispatchers.IO) {
        dao.updateFlagged(itemId, flagged)
    }

    suspend fun resetAll() = withContext(Dispatchers.IO) {
        dao.resetAll()
    }

}


//Author: Mangesana E
//Student number: 2030630053