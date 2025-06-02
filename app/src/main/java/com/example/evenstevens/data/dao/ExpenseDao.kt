package com.example.evenstevens.data.dao

import androidx.room.*
import com.example.evenstevens.data.database.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: ExpenseEntity)

    @Update
    suspend fun update(expense: ExpenseEntity)

    @Delete
    suspend fun delete(expense: ExpenseEntity)

    @Query("SELECT * FROM ExpenseEntity WHERE id = :expenseId")
    suspend fun getById(expenseId: Int): ExpenseEntity?

    @Query("SELECT * FROM ExpenseEntity")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>
}
