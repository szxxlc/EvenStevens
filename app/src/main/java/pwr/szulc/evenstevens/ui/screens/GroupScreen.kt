package pwr.szulc.evenstevens.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pwr.szulc.evenstevens.data.viewmodels.*
import pwr.szulc.evenstevens.ui.common.AppTopBar

@Composable
fun GroupScreen(
    groupId: Int,
    navController: NavController,
    groupViewModel: GroupViewModel,
    expenseViewModel: ExpenseViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current

    val group by groupViewModel.getGroupById(groupId).collectAsState(initial = null)
    val expenses by expenseViewModel.getExpensesByGroup(groupId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            AppTopBar(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_expense/$groupId") }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Dodaj wydatek")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            group?.let {
                Text("Grupa: ${it.name}", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        // tu dodac edytowanie grupy
                        Toast.makeText(context, "Edycja jeszcze niezaimplementowana", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edytuj")
                        Spacer(modifier = Modifier.width(8.dp))
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
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Usuń")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Wydatki:", style = MaterialTheme.typography.titleMedium)
                if (expenses.isEmpty()) {
                    Text("Brak wydatków w tej grupie.")
                } else {
                    expenses.forEach { expense ->
                        Text("• ${expense.name}: ${expense.amount} zł")
                    }
                }
            } ?: run {
                Text("Nie znaleziono grupy.")
            }
        }
    }
}
