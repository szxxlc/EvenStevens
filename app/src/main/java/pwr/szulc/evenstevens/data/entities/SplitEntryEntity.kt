package pwr.szulc.evenstevens.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "split_entries")
data class SplitEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val expenseId: Int,
    val userId: Int,
    val amount: Double
)