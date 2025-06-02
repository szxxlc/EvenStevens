package com.example.evenstevens.data.dao

import androidx.room.*
import com.example.evenstevens.data.database.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE age > :minAge")
    suspend fun loadAllUsersOlderThan(minAge: Int): List<User>

    @Query("SELECT first_name, last_name FROM user")
    suspend fun loadFullName(): List<NameTuple>

    @Query("SELECT * FROM user WHERE region IN (:regions)")
    suspend fun loadUsersFromRegions(regions: List<String>): List<User>
}
