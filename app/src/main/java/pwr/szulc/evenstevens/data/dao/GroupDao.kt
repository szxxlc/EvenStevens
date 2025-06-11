package pwr.szulc.evenstevens.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pwr.szulc.evenstevens.data.entities.GroupEntity

@Dao
interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(group: GroupEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAndReturnId(group: GroupEntity): Long

    @Update
    suspend fun update(group: GroupEntity)

    @Delete
    suspend fun delete(group: GroupEntity)

    @Query("SELECT * FROM `groups` WHERE id = :groupId")
    suspend fun getGroupById(groupId: Int): GroupEntity?

    @Query("SELECT * FROM `groups` WHERE id = :groupId")
    fun getByIdFlow(groupId: Int): Flow<GroupEntity?>

    @Query("SELECT * FROM `groups`")
    fun getAllGroups(): Flow<List<GroupEntity>>

}
