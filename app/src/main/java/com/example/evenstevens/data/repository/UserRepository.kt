package com.example.evenstevens.data.repository

import com.example.evenstevens.data.dao.UserDao
import com.example.evenstevens.data.database.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    val allUsers: Flow<List<UserEntity>> = userDao.getAllUsers()

    suspend fun insert(user: UserEntity) {
        userDao.insert(user)
    }

    suspend fun delete(user: UserEntity) {
        userDao.delete(user)
    }

    suspend fun getUserById(id: Int): UserEntity? {
        return userDao.getUserById(id)
    }
}
