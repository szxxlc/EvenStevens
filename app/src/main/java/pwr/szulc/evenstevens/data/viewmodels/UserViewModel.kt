package pwr.szulc.evenstevens.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pwr.szulc.evenstevens.data.repositories.UserRepository
import pwr.szulc.evenstevens.data.entities.UserEntity

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    val users = repository.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addUser(user: UserEntity) {
        viewModelScope.launch { repository.insert(user) }
    }
}
