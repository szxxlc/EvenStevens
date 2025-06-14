package pwr.szulc.evenstevens.data.repositories

import kotlinx.coroutines.flow.Flow
import pwr.szulc.evenstevens.data.dao.ExpenseDao
import pwr.szulc.evenstevens.data.entities.ExpenseEntity
import pwr.szulc.evenstevens.data.dao.SplitEntryDao
import pwr.szulc.evenstevens.data.entities.SplitEntryEntity
import kotlin.collections.map

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val splitEntryDao: SplitEntryDao
) {

    fun getAllExpenses(): Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()

    fun getExpensesByGroup(groupId: Int): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesByGroup(groupId)

    fun getTotalAmountPaidByUser(userId: Int): Flow<Double?> =
        expenseDao.getTotalAmountPaidByUser(userId)

    fun getTotalAmountByUserInGroup(groupId: Int, userId: Int): Flow<Double?> =
        expenseDao.getTotalAmountByUserInGroup(groupId, userId)

    suspend fun insertExpense(expense: ExpenseEntity) = expenseDao.insertExpense(expense)

    suspend fun insertExpenseWithSplits(expense: ExpenseEntity, splits: List<SplitEntryEntity>) {
        val expenseId = expenseDao.insertExpense(expense).toInt()
        val updatedSplits = splits.map { it.copy(expenseId = expenseId) }
        splitEntryDao.insertAll(updatedSplits)
    }

    fun getCategoriesByGroup(groupId: Int): Flow<List<String>> =
        expenseDao.getCategoriesByGroup(groupId)

    suspend fun updateExpense(expense: ExpenseEntity) = expenseDao.updateExpense(expense)

    suspend fun deleteExpense(expense: ExpenseEntity) = expenseDao.deleteExpense(expense)

}
