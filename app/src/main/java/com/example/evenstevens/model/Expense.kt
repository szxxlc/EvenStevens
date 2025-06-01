data class Expense(
    val id: Int,
    val groupId: Int,
    val name: String,
    val category: String?,
    val amount: Double,
    val paidBy: Int, // userId
    val splitBetween: List<SplitEntry>
)
