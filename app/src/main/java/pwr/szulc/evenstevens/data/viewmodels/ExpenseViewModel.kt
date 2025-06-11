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

    val allExpenses = repository.getAllExpenses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getExpensesByGroup(groupId: Int): StateFlow<List<ExpenseEntity>> {
        return repository.getExpensesByGroup(groupId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun getTotalPaidByUser(userId: Int): StateFlow<Double?> {
        return repository.getTotalAmountPaidByUser(userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    }

    fun getCategoriesByGroup(groupId: Int): Flow<List<String>> =
        repository.getCategoriesByGroup(groupId)


    fun getTotalByUserInGroup(groupId: Int, userId: Int): StateFlow<Double?> {
        return repository.getTotalAmountByUserInGroup(groupId, userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    }

    fun addExpense(expense: ExpenseEntity) {
        viewModelScope.launch { repository.insertExpense(expense) }
    }

    fun addExpenseWithSplits(expense: ExpenseEntity, splits: List<SplitEntryEntity>) {
        viewModelScope.launch {
            repository.insertExpenseWithSplits(expense, splits)
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch { repository.deleteExpense(expense) }
    }

    fun updateExpense(expense: ExpenseEntity) {
        viewModelScope.launch { repository.updateExpense(expense) }
    }
}
