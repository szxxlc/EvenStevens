package pwr.szulc.evenstevens.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pwr.szulc.evenstevens.data.entities.SplitEntryEntity
import pwr.szulc.evenstevens.data.entities.ExpenseEntity

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

    @Query("""
    SELECT se.* FROM split_entries AS se
    INNER JOIN expenses AS e ON se.expenseId = e.id
    WHERE e.groupId = :groupId
    """)
    fun getSplitEntriesByGroup(groupId: Int): Flow<List<SplitEntryEntity>>

    @Query("""
    SELECT COUNT(*) FROM split_entries
    INNER JOIN expenses ON split_entries.expenseId = expenses.id
    WHERE split_entries.userId = :userId AND expenses.groupId = :groupId
    """)
    suspend fun countSplitEntriesForUserInGroup(userId: Int, groupId: Int): Int

}