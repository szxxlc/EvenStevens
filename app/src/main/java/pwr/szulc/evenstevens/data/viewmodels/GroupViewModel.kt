package pwr.szulc.evenstevens.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pwr.szulc.evenstevens.data.repositories.GroupRepository
import pwr.szulc.evenstevens.data.entities.GroupEntity

class GroupViewModel(private val repository: GroupRepository) : ViewModel() {

    val groups = repository.getAllGroups()
        .map { it.sortedBy { group -> group.name } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addGroup(group: GroupEntity, onGroupInserted: (Int) -> Unit) {
        viewModelScope.launch {
            val id = repository.insertAndReturnId(group)
            onGroupInserted(id)
        }
    }

    fun deleteGroup(group: GroupEntity) {
        viewModelScope.launch { repository.delete(group) }
    }

    fun updateGroup(group: GroupEntity) {
        viewModelScope.launch { repository.update(group) }
    }

    fun getGroupById(groupId: Int): Flow<GroupEntity?> = repository.getGroupById(groupId)

}
