package pwr.szulc.evenstevens.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pwr.szulc.evenstevens.data.repositories.ExpenseRepository
import pwr.szulc.evenstevens.data.entities.ExpenseEntity
import pwr.szulc.evenstevens.data.entities.SplitEntryEntity

class ExpenseViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {

    fun getExpensesByGroup(groupId: Int): StateFlow<List<ExpenseEntity>> {
        return repository.getExpensesByGroup(groupId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun getCategoriesByGroup(groupId: Int): Flow<List<String>> =
        repository.getCategoriesByGroup(groupId)

    fun addExpenseWithSplits(expense: ExpenseEntity, splits: List<SplitEntryEntity>) {
        viewModelScope.launch {
            repository.insertExpenseWithSplits(expense, splits)
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch { repository.deleteExpense(expense) }
    }

    fun getTotalPaidByUser(userId: Int): StateFlow<Double?> {
        return repository.getTotalAmountPaidByUser(userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    }
}
