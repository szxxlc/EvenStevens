package pwr.szulc.evenstevens.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pwr.szulc.evenstevens.data.repositories.GroupUserCrossRefRepository
import pwr.szulc.evenstevens.data.entities.GroupUserCrossRef

class GroupUserCrossRefViewModel(
    private val repository: GroupUserCrossRefRepository
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

    fun getUsersForGroup(groupId: Int): Flow<List<GroupUserCrossRef>> {
        return repository.getUsersForGroup(groupId)
    }

    fun getGroupsForUser(userId: Int): Flow<List<GroupUserCrossRef>> {
        return repository.getGroupsForUser(userId)
    }
}
