package pwr.szulc.evenstevens.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pwr.szulc.evenstevens.data.repositories.GroupUserCrossRefRepository
import pwr.szulc.evenstevens.data.entities.GroupUserCrossRef
import pwr.szulc.evenstevens.data.entities.UserEntity
import pwr.szulc.evenstevens.data.dao.GroupUserCrossRefDao

class GroupUserCrossRefViewModel(
    private val repository: GroupUserCrossRefRepository,
    private val dao: GroupUserCrossRefDao
) : ViewModel() {

    fun addUserToGroup(groupId: Int, userId: Int) {
        viewModelScope.launch {
            val crossRef = GroupUserCrossRef(groupId = groupId, userId = userId)
            repository.insert(crossRef)
        }
    }

    fun removeUserFromGroup(groupId: Int, userId: Int) {
        viewModelScope.launch {
            repository.deleteByGroupAndUser(groupId, userId)
        }
    }

    fun getUsersForGroup(groupId: Int): Flow<List<UserEntity>> {
        return repository.getUsersForGroup(groupId)
    }

    fun getGroupsForUser(userId: Int): Flow<List<GroupUserCrossRef>> {
        return repository.getGroupsForUser(userId)
    }

    suspend fun getUsersForGroupSync(groupId: Int): List<UserEntity> {
        return dao.getUsersForGroupSync(groupId)
    }

    suspend fun canUserBeRemovedFromGroupSuspend(userId: Int, groupId: Int): Boolean {
        return repository.canUserBeRemovedSuspend(userId, groupId)
    }

}
