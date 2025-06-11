package pwr.szulc.evenstevens.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pwr.szulc.evenstevens.data.entities.SplitEntryEntity

@Dao
interface SplitEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(splitEntry: SplitEntryEntity)

    @Insert
    suspend fun insertAll(splits: List<SplitEntryEntity>)

    @Update
    suspend fun update(splitEntry: SplitEntryEntity)

    @Delete
    suspend fun delete(splitEntry: SplitEntryEntity)

    @Query("SELECT * FROM split_entries WHERE id = :splitEntryId")
    suspend fun getById(splitEntryId: Int): SplitEntryEntity?

    @Query("SELECT * FROM split_entries")
    fun getAllSplitEntries(): Flow<List<SplitEntryEntity>>
}