package pwr.szulc.evenstevens.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pwr.szulc.evenstevens.data.entities.ExpenseEntity
import pwr.szulc.evenstevens.data.entities.SplitEntryEntity

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE groupId = :groupId")
    fun getExpensesByGroup(groupId: Int): Flow<List<ExpenseEntity>>

    @Query("SELECT SUM(amount) FROM expenses WHERE paidByUserId = :userId")
    fun getTotalAmountPaidByUser(userId: Int): Flow<Double?>

    @Query("SELECT SUM(amount) FROM expenses WHERE groupId = :groupId AND paidByUserId = :userId")
    fun getTotalAmountByUserInGroup(groupId: Int, userId: Int): Flow<Double?>

    @Query("SELECT DISTINCT category FROM expenses WHERE groupId = :groupId AND category != ''")
    fun getCategoriesByGroup(groupId: Int): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM expenses WHERE paidByUserId = :userId AND groupId = :groupId")
    suspend fun countPaidByUserInGroup(userId: Int, groupId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity): Long

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expenses")
    suspend fun deleteAll()
}
