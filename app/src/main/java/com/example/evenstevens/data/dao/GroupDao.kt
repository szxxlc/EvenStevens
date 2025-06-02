package com.example.evenstevens.data.dao

import androidx.room.*
import com.example.evenstevens.data.database.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(group: GroupEntity)

    @Update
    suspend fun update(group: GroupEntity)

    @Delete
    suspend fun delete(group: GroupEntity)

    @Query("SELECT * FROM GroupEntity WHERE id = :groupId")
    suspend fun getById(groupId: Int): GroupEntity?

    @Query("SELECT * FROM GroupEntity")
    fun getAllGroups(): Flow<List<GroupEntity>>
}
