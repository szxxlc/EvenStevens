package pwr.szulc.evenstevens.data.repositories

import kotlinx.coroutines.flow.Flow
import pwr.szulc.evenstevens.data.dao.UserDao
import pwr.szulc.evenstevens.data.entities.UserEntity

class UserRepository(private val userDao: UserDao) {

    suspend fun insert(user: UserEntity) = userDao.insert(user)

    suspend fun update(user: UserEntity) = userDao.update(user)

    suspend fun delete(user: UserEntity) = userDao.delete(user)

    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()
}
