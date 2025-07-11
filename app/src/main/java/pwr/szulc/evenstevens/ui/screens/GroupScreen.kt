package pwr.szulc.evenstevens.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pwr.szulc.evenstevens.data.entities.ExpenseEntity
import pwr.szulc.evenstevens.data.entities.SplitEntryEntity
import pwr.szulc.evenstevens.data.entities.UserEntity
import pwr.szulc.evenstevens.data.viewmodels.*
import pwr.szulc.evenstevens.ui.common.AppTopBar

@Composable
fun GroupScreen(
    groupId: Int,
    navController: NavController,
    userViewModel: UserViewModel,
    groupViewModel: GroupViewModel,
    expenseViewModel: ExpenseViewModel,
    groupUserCrossRefViewModel: GroupUserCrossRefViewModel,
    splitEntryViewModel: SplitEntryViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current

    val group by groupViewModel.getGroupById(groupId).collectAsState(initial = null)
    val expenses by expenseViewModel.getExpensesByGroup(groupId).collectAsState(initial = emptyList())
    val groupUsers by groupUserCrossRefViewModel.getUsersForGroup(groupId).collectAsState(initial = emptyList())
    val splitEntries by splitEntryViewModel.getSplitEntriesByGroup(groupId).collectAsState(initial = emptyList())
    val allUsers by userViewModel.users.collectAsState(initial = emptyList())

    val expandedUsers = remember { mutableStateMapOf<Int, Boolean>() }

    Scaffold(
        topBar = {
            AppTopBar(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add_expense/$groupId")
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Dodaj wydatek")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 48.dp),
        ) {
            group?.let {
                Text("Grupa: ${it.name}", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = {
                        navController.navigate("edit_group/$groupId")
                    }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edytuj")
                        Spacer(Modifier.width(8.dp))
                        Text("Edytuj")
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        onClick = {
                            groupViewModel.deleteGroup(it)
                            Toast.makeText(context, "Usunięto grupę", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Usuń")
                        Spacer(Modifier.width(8.dp))
                        Text("Usuń")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                val balances = remember(expenses, splitEntries, groupId) {
                    calculateBalances(expenses, splitEntries, groupId)
                }

                UserBalanceSection(groupUsers, balances, expandedUsers)
                Spacer(modifier = Modifier.height(24.dp))
                ExpenseListSection(expenses, allUsers, expenseViewModel, context)
            } ?: run {
                Text("Nie znaleziono grupy.")
            }
        }
    }
}

fun calculateBalances(
    expenses: List<ExpenseEntity>,
    splitEntries: List<SplitEntryEntity>,
    groupId: Int
): Map<Int, Double> {
    if (expenses.isEmpty() || splitEntries.isEmpty()) return emptyMap()

    val result = mutableMapOf<Int, Double>()

    expenses.filter { it.groupId == groupId }.forEach { expense ->
        val paid = expense.amount
        val paidBy = expense.paidByUserId
        result[paidBy] = (result[paidBy] ?: 0.0) + paid
    }

    splitEntries.filter { entry ->
        val expense = expenses.find { it.id == entry.expenseId }
        expense?.groupId == groupId
    }.forEach { entry ->
        result[entry.userId] = (result[entry.userId] ?: 0.0) - entry.amount
    }

    return result
}

@Composable
fun UserBalanceSection(
    groupUsers: List<UserEntity>,
    balances: Map<Int, Double>,
    expandedUsers: MutableMap<Int, Boolean>
) {
    Text("Saldo użytkowników:", style = MaterialTheme.typography.titleMedium)

    groupUsers.forEach { user ->
        val balance = balances[user.id] ?: 0.0
        val expanded = expandedUsers[user.id] ?: false
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expandedUsers[user.id] = !expanded }
                .padding(vertical = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(user.name)
                Text(
                    text = "%.2f zł".format(balance),
                    color = if (balance < 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }

            if (expanded) {
                if (balance == 0.0) {
                    Text("Rozliczony/a", style = MaterialTheme.typography.bodySmall)
                } else {
                    val targets = if (balance < 0) {
                        balances.filter { it.value > 0 }
                    } else {
                        balances.filter { it.value < 0 }
                    }

                    val totalOwed = targets.values.sumOf { kotlin.math.abs(it) }

                    targets.forEach { (targetId, targetBalance) ->
                        val percent = kotlin.math.abs(targetBalance) / totalOwed
                        val transferAmount = kotlin.math.abs(balance) * percent
                        val targetUser = groupUsers.find { it.id == targetId }
                        if (targetUser != null) {
                            val direction = if (balance < 0) "→ ${targetUser.name}" else "← ${targetUser.name}"
                            Text("   %.2f zł %s".format(transferAmount, direction))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseListSection(
    expenses: List<ExpenseEntity>,
    allUsers: List<UserEntity>,
    expenseViewModel: ExpenseViewModel,
    context: Context
) {
    Text("Wydatki:", style = MaterialTheme.typography.titleMedium)

    if (expenses.isEmpty()) {
        Text("Brak wydatków w tej grupie.")
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 4.dp),
        ) {
            items(expenses, key = { it.id }) { expense ->
                ExpenseRow(
                    expense = expense,
                    allUsers = allUsers,
                    onDelete = {
                        expenseViewModel.deleteExpense(expense)
                        Toast.makeText(context, "Usunięto wydatek", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Composable
fun ExpenseRow(
    expense: ExpenseEntity,
    allUsers: List<UserEntity>,
    onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column {
            Text("${expense.name} – %.2f zł".format(expense.amount))

            val payerName = allUsers.find { it.id == expense.paidByUserId }?.name ?: "Nieznany"
            val categoryLabel = if (expense.category.isNullOrBlank()) "inne" else expense.category
            Text(
                "Kategoria: ${categoryLabel}, zapłacił/a: $payerName",
                style = MaterialTheme.typography.bodySmall
            )
        }

        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Usuń wydatek")
        }
    }
}
