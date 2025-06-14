package pwr.szulc.evenstevens.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import pwr.szulc.evenstevens.data.repositories.SplitEntryRepository
import pwr.szulc.evenstevens.data.entities.SplitEntryEntity

class SplitEntryViewModel(
    private val repository: SplitEntryRepository
) : ViewModel() {

    fun getSplitEntriesByGroup(groupId: Int): Flow<List<SplitEntryEntity>> {
        return repository.getSplitEntriesByGroup(groupId)
    }

    fun getTotalOwedByUser(userId: Int): StateFlow<Double?> {
        return repository.getTotalAmountOwedByUser(userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    }


}
