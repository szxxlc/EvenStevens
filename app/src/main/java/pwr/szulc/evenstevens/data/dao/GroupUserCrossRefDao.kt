package pwr.szulc.evenstevens.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pwr.szulc.evenstevens.data.entities.GroupUserCrossRef

@Dao
interface GroupUserCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(crossRef: GroupUserCrossRef)

    @Query("SELECT * FROM GroupUserCrossRef WHERE groupId = :groupId")
    fun getUsersInGroup(groupId: Int): Flow<List<GroupUserCrossRef>>

    @Query("SELECT * FROM GroupUserCrossRef WHERE userId = :userId")
    fun getGroupsOfUser(userId: Int): Flow<List<GroupUserCrossRef>>

    @Query("DELETE FROM GroupUserCrossRef WHERE groupId = :groupId AND userId = :userId")
    suspend fun deleteByGroupAndUser(groupId: Int, userId: Int)

}
