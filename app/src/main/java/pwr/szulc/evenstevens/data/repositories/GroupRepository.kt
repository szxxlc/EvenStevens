package pwr.szulc.evenstevens.data.repositories

import kotlinx.coroutines.flow.Flow
import pwr.szulc.evenstevens.data.dao.GroupDao
import pwr.szulc.evenstevens.data.dao.GroupUserCrossRefDao
import pwr.szulc.evenstevens.data.entities.GroupEntity
import pwr.szulc.evenstevens.data.entities.GroupUserCrossRef

class GroupRepository(
    private val groupDao: GroupDao
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

}

