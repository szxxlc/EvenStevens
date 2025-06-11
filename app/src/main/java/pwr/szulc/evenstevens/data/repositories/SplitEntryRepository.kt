package pwr.szulc.evenstevens.data.repositories

import kotlinx.coroutines.flow.Flow
import pwr.szulc.evenstevens.data.dao.SplitEntryDao
import pwr.szulc.evenstevens.data.entities.SplitEntryEntity

class SplitEntryRepository(private val splitEntryDao: SplitEntryDao) {

    suspend fun insert(splitEntry: SplitEntryEntity) = splitEntryDao.insert(splitEntry)

    suspend fun insertAll(splits: List<SplitEntryEntity>) {
        splitEntryDao.insertAll(splits)
    }

    suspend fun update(splitEntry: SplitEntryEntity) = splitEntryDao.update(splitEntry)

    suspend fun delete(splitEntry: SplitEntryEntity) = splitEntryDao.delete(splitEntry)

    suspend fun getById(splitEntryId: Int): SplitEntryEntity? = splitEntryDao.getById(splitEntryId)

    fun getAllSplitEntries(): Flow<List<SplitEntryEntity>> = splitEntryDao.getAllSplitEntries()

}
