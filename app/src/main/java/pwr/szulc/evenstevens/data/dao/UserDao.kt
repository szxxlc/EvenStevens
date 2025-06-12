package pwr.szulc.evenstevens.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pwr.szulc.evenstevens.data.entities.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("SELECT * FROM `users` WHERE id = :userId")
    suspend fun getById(userId: Int): UserEntity?

    @Query("SELECT * FROM `users`")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("""
    SELECT * FROM users WHERE id IN (
        SELECT userId FROM GroupUserCrossRef  WHERE groupId = :groupId
    )
""")
    fun getUsersForGroup(groupId: Int): Flow<List<UserEntity>>

}
