package com.example.evenstevens.data.repository

import com.example.evenstevens.data.dao.SplitEntryDao
import com.example.evenstevens.data.database.SplitEntryEntity
import kotlinx.coroutines.flow.Flow

class SplitEntryRepository(private val splitEntryDao: SplitEntryDao) {

    val allSplitEntries: Flow<List<SplitEntryEntity>> = splitEntryDao.getAllSplitEntries()

    suspend fun insert(splitEntry: SplitEntryEntity) {
        splitEntryDao.insert(splitEntry)
    }

    suspend fun update(splitEntry: SplitEntryEntity) {
        splitEntryDao.update(splitEntry)
    }

    suspend fun delete(splitEntry: SplitEntryEntity) {
        splitEntryDao.delete(splitEntry)
    }
}
