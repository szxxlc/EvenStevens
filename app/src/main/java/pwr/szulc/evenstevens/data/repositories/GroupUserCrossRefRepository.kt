package pwr.szulc.evenstevens.data.repositories

import pwr.szulc.evenstevens.data.dao.GroupUserCrossRefDao
import pwr.szulc.evenstevens.data.entities.GroupUserCrossRef
import pwr.szulc.evenstevens.data.dao.UserDao
import pwr.szulc.evenstevens.data.entities.UserEntity
import kotlinx.coroutines.flow.Flow

class GroupUserCrossRefRepository(private val dao: GroupUserCrossRefDao, private val userDao: UserDao) {

    suspend fun insert(crossRef: GroupUserCrossRef) {
        dao.insert(crossRef)
    }

    suspend fun deleteByGroupAndUser(groupId: Int, userId: Int) {
        dao.deleteByGroupAndUser(groupId, userId)
    }

    fun getUsersForGroup(groupId: Int): Flow<List<UserEntity>> {
        return userDao.getUsersForGroup(groupId)
    }

    fun getGroupsForUser(userId: Int): Flow<List<GroupUserCrossRef>> {
        return dao.getGroupsOfUser(userId)
    }
}
