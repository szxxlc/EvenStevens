package com.example.evenstevens.data.dao

import androidx.room.*
import com.example.evenstevens.data.database.SplitEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SplitEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(splitEntry: SplitEntryEntity)

    @Update
    suspend fun update(splitEntry: SplitEntryEntity)

    @Delete
    suspend fun delete(splitEntry: SplitEntryEntity)

    @Query("SELECT * FROM SplitEntryEntity WHERE id = :splitEntryId")
    suspend fun getById(splitEntryId: Int): SplitEntryEntity?

    @Query("SELECT * FROM SplitEntryEntity")
    fun getAllSplitEntries(): Flow<List<SplitEntryEntity>>
}
