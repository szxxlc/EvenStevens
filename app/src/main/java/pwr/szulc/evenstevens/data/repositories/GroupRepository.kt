package pwr.szulc.evenstevens.data.repositories

import kotlinx.coroutines.flow.Flow
import pwr.szulc.evenstevens.data.dao.GroupDao
import pwr.szulc.evenstevens.data.dao.GroupUserCrossRefDao
import pwr.szulc.evenstevens.data.entities.GroupEntity
import pwr.szulc.evenstevens.data.entities.GroupUserCrossRef

class GroupRepository(
    private val groupDao: GroupDao,
    private val groupUserCrossRefDao: GroupUserCrossRefDao
) {
    fun getAllGroups(): Flow<List<GroupEntity>> = groupDao.getAllGroups()

    fun getGroupById(groupId: Int): Flow<GroupEntity?> =
        groupDao.getByIdFlow(groupId)

    suspend fun insert(group: GroupEntity) = groupDao.insert(group)

    suspend fun insertAndReturnId(group: GroupEntity): Int {
        return groupDao.insertAndReturnId(group).toInt()
    }

    suspend fun update(group: GroupEntity) = groupDao.update(group)

    suspend fun delete(group: GroupEntity) = groupDao.delete(group)

    suspend fun addUserToGroup(groupId: Int, userId: Int) {
        groupUserCrossRefDao.insert(GroupUserCrossRef(groupId = groupId, userId = userId))
    }

    suspend fun removeUserFromGroup(groupId: Int, userId: Int) {
        groupUserCrossRefDao.deleteByGroupAndUser(groupId, userId)
    }

    fun getUsersInGroup(groupId: Int) = groupUserCrossRefDao.getUsersInGroup(groupId)

    fun getGroupsOfUser(userId: Int) = groupUserCrossRefDao.getGroupsOfUser(userId)
}

