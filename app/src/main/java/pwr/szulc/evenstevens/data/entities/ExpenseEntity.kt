package pwr.szulc.evenstevens.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val groupId: Int,
    val name: String,
    val category: String?,
    val amount: Double,
    val paidByUserId: Int
)