package pwr.szulc.evenstevens.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pwr.szulc.evenstevens.data.repositories.SplitEntryRepository
import pwr.szulc.evenstevens.data.entities.SplitEntryEntity

class SplitEntryViewModel(private val repository: SplitEntryRepository) : ViewModel() {

    val allSplitEntries = repository.getAllSplitEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addEntry(entry: SplitEntryEntity) {
        viewModelScope.launch { repository.insert(entry) }
    }

    fun updateEntry(entry: SplitEntryEntity) {
        viewModelScope.launch { repository.update(entry) }
    }

    fun deleteEntry(entry: SplitEntryEntity) {
        viewModelScope.launch { repository.delete(entry) }
    }

    fun insertAll(splits: List<SplitEntryEntity>) {
        viewModelScope.launch {
            repository.insertAll(splits)
        }
    }

}
