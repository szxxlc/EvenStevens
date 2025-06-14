package pwr.szulc.evenstevens.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import pwr.szulc.evenstevens.data.viewmodels.*
import pwr.szulc.evenstevens.ui.common.AppTopBar

@Composable
fun HomeScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    groupUserCrossRefViewModel: GroupUserCrossRefViewModel,
    expenseViewModel: ExpenseViewModel,
    splitEntryViewModel: SplitEntryViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val users by userViewModel.users.collectAsState(initial = emptyList())
    var selectedUser by remember { mutableStateOf<String?>(null) }
    val selectedUserId = users.find { it.name == selectedUser }?.id

    var totalPaid by remember { mutableStateOf<Double?>(null) }
    var totalOwed by remember { mutableStateOf<Double?>(null) }
    var totalBalance by remember { mutableStateOf<Double?>(null) }

    LaunchedEffect(selectedUserId) {
        if (selectedUserId != null) {
            expenseViewModel.getTotalPaidByUser(selectedUserId).collectLatest { paidValue ->
                splitEntryViewModel.getTotalOwedByUser(selectedUserId).collectLatest { owedValue ->
                    totalPaid = paidValue ?: 0.0
                    totalOwed = owedValue ?: 0.0
                    totalBalance = totalPaid!! - totalOwed!!
                }
            }
        }
    }


    Scaffold(
        topBar = {
            AppTopBar(isDarkTheme = isDarkTheme, onToggleTheme = onToggleTheme)
        },
        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { navController.navigate("group_list") }) {
                    Icon(Icons.Filled.Group, contentDescription = "Grupy")
                    Spacer(Modifier.width(8.dp))
                    Text("Grupy")
                }
                Button(onClick = { navController.navigate("author_info") }) {
                    Icon(Icons.Filled.Info, contentDescription = "Autor")
                    Spacer(Modifier.width(8.dp))
                    Text("O autorze")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text("Wybierz użytkownika", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            var expanded by remember { mutableStateOf(false) }

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(selectedUser ?: "Wybierz użytkownika")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    users.forEach { user ->
                        DropdownMenuItem(
                            text = { Text(user.name) },
                            onClick = {
                                selectedUser = user.name
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (selectedUser != null) {
                Text("Witaj, $selectedUser!", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))

//                when {
//                    totalBalance == null -> Text("Trwa obliczanie salda...")
//                    totalBalance!! > 0 -> Text("Inni są Ci winni %.2f zł".format(totalBalance), color = MaterialTheme.colorScheme.primary)
//                    totalBalance!! < 0 -> Text("Jesteś winien/winna innym %.2f zł".format(-totalBalance!!), color = MaterialTheme.colorScheme.error)
//                    else -> Text("Jesteś rozliczony/a ze wszystkimi.", color = MaterialTheme.colorScheme.onBackground)
//                }

                Text("Inni są Ci winni %.2f zł".format(totalPaid), color = MaterialTheme.colorScheme.primary)
                Text("Jesteś winien/winna innym %.2f zł".format(totalOwed), color = MaterialTheme.colorScheme.error)
                Text("Twoj balans to %.2f zł".format(totalBalance), color = MaterialTheme.colorScheme.onBackground)

            }
        }
    }
}
