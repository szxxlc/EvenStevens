package pwr.szulc.evenstevens.data

import androidx.room.Database
import androidx.room.RoomDatabase
import pwr.szulc.evenstevens.data.dao.*
import pwr.szulc.evenstevens.data.entities.*

@Database(
    entities = [
        ExpenseEntity::class,
        GroupEntity::class,
        UserEntity::class,
        SplitEntryEntity::class,
        GroupUserCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
    abstract fun groupDao(): GroupDao
    abstract fun userDao(): UserDao
    abstract fun splitEntryDao(): SplitEntryDao
    abstract fun groupUserCrossRefDao(): GroupUserCrossRefDao
}
