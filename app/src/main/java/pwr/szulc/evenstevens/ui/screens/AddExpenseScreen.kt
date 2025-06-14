package pwr.szulc.evenstevens.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pwr.szulc.evenstevens.data.entities.ExpenseEntity
import pwr.szulc.evenstevens.data.viewmodels.*
import pwr.szulc.evenstevens.ui.common.AppTopBar
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import pwr.szulc.evenstevens.data.entities.SplitEntryEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    groupId: Int,
    navController: NavController,
    expenseViewModel: ExpenseViewModel,
    userViewModel: UserViewModel,
    groupUserCrossRefViewModel: GroupUserCrossRefViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current

    var expenseName by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    val usersInGroup by groupUserCrossRefViewModel.getUsersForGroup(groupId).collectAsState(initial = emptyList())
    val allUsers by userViewModel.users.collectAsState(initial = emptyList())

    val availableUsers = usersInGroup

    var expanded by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<String?>(null) }

    val selectedUsers = remember { mutableStateMapOf<Int, Boolean>() }
    availableUsers.forEach { user -> selectedUsers.putIfAbsent(user.id, false) }

    var splitEqually by remember { mutableStateOf(true) }
    var customSplit by remember { mutableStateOf(false) }
    val customAmounts = remember { mutableStateMapOf<Int, String>() }

    val scrollState = rememberScrollState()

    val existingCategories by expenseViewModel.getCategoriesByGroup(groupId).collectAsState(initial = emptyList())
    var categoryExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 48.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text("Dodaj wydatek", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = expenseName,
                onValueChange = { expenseName = it },
                label = { Text("Nazwa wydatku") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = expenseAmount,
                onValueChange = { expenseAmount = it },
                label = { Text("Kwota [zł]") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            val existingCategories by expenseViewModel.getCategoriesByGroup(groupId).collectAsState(initial = emptyList())
            var categoryExpanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Kategoria") },
                    readOnly = false,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    existingCategories.distinct().forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                category = cat
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(12.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = selectedUser ?: "Wybierz płacącego",
                    onValueChange = {},
                    label = { Text("Kto zapłacił") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    availableUsers.forEach { user ->
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

            Spacer(modifier = Modifier.height(16.dp))
            Text("Za kogo płacono:")
            availableUsers.forEach { user ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = selectedUsers[user.id] == true,
                        onCheckedChange = { checked ->
                            selectedUsers[user.id] = checked

                            val selectedCount = selectedUsers.count { it.value } +
                                    if (selectedUser != null && selectedUsers[availableUsers.find { it.name == selectedUser }?.id] != true) 1 else 0

                            if (selectedCount <= 1) {
                                splitEqually = true
                                customSplit = false
                            }
                        }
                    )

                    Text(user.name)
                }
            }

            val numberOfSelected = selectedUsers.count { it.value } +
                    if (selectedUser != null && selectedUsers[availableUsers.find { it.name == selectedUser }?.id] != true) 1 else 0

            if (numberOfSelected > 1) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Jak podzielić koszt:")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = splitEqually,
                        onCheckedChange = {
                            splitEqually = it
                            if (it) customSplit = false
                        }
                    )
                    Text("Po równo")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = customSplit,
                        onCheckedChange = {
                            customSplit = it
                            if (it) splitEqually = false
                        }
                    )
                    Text("Niestandardowo")
                }
            }


            if (customSplit) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("Podaj kwoty dla użytkowników:")
                selectedUsers.filterValues { it }.keys.forEach { userId ->
                    val user = availableUsers.find { it.id == userId }
                    if (user != null) {
                        OutlinedTextField(
                            value = customAmounts[userId] ?: "",
                            onValueChange = { customAmounts[userId] = it },
                            label = { Text(user.name) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val amount = expenseAmount.toDoubleOrNull()
                    val paidByUserId = availableUsers.find { it.name == selectedUser }?.id
                    val includedUserIds = selectedUsers.filterValues { it }.keys

                    if (
                        expenseName.isNotBlank() &&
                        amount != null && amount > 0 &&
                        paidByUserId != null &&
                        includedUserIds.isNotEmpty()
                    )  {
                        if (!(splitEqually || customSplit)) {
                            Toast.makeText(context, "Wybierz sposób podziału kosztów", Toast.LENGTH_LONG).show()
                            return@Button
                        }

                        if (customSplit) {
                            val sum = includedUserIds.sumOf {
                                customAmounts[it]?.toDoubleOrNull() ?: 0.0
                            }
                            if (sum != amount) {
                                Toast.makeText(context, "Suma kwot nie równa się pełnej kwocie", Toast.LENGTH_LONG).show()
                                return@Button
                            }
                        }

                        val expense = ExpenseEntity(
                            name = expenseName,
                            amount = amount,
                            groupId = groupId,
                            category = category,
                            paidByUserId = paidByUserId
                        )

                        val splits = if (splitEqually) {
                            val share = amount / includedUserIds.size
                            includedUserIds.map { userId ->
                                SplitEntryEntity(
                                    expenseId = 0,
                                    userId = userId,
                                    amount = share
                                )
                            }
                        } else {
                            includedUserIds.mapNotNull { userId ->
                                val customAmount = customAmounts[userId]?.toDoubleOrNull()
                                if (customAmount != null) {
                                    SplitEntryEntity(
                                        expenseId = 0,
                                        userId = userId,
                                        amount = customAmount
                                    )
                                } else null
                            }
                        }

                        expenseViewModel.addExpenseWithSplits(expense, splits)

                        Toast.makeText(context, "Dodano wydatek", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Wprowadź poprawne dane", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Zapisz")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Zapisz")
            }
        }
    }
}
