package com.example.evenstevens.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.evenstevens.data.dao.*
import com.example.evenstevens.data.database.*

@Database(
    entities = [
        UserEntity::class,
        GroupEntity::class,
        ExpenseEntity::class,
        SplitEntryEntity::class,
        GroupUserCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun groupDao(): GroupDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun splitEntryDao(): SplitEntryDao
}
