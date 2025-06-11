package pwr.szulc.evenstevens.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pwr.szulc.evenstevens.data.entities.GroupEntity
import pwr.szulc.evenstevens.data.entities.UserEntity
import pwr.szulc.evenstevens.data.viewmodels.GroupUserCrossRefViewModel
import pwr.szulc.evenstevens.data.viewmodels.GroupViewModel
import pwr.szulc.evenstevens.data.viewmodels.UserViewModel
import pwr.szulc.evenstevens.ui.common.AppTopBar

@Composable
fun AddGroupScreen(
    navController: NavController,
    groupViewModel: GroupViewModel,
    userViewModel: UserViewModel,
    groupUserCrossRefViewModel: GroupUserCrossRefViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    var groupName by remember { mutableStateOf("") }

    val users by userViewModel.users.collectAsState(initial = emptyList())
    val selectedUserIds = remember { mutableStateListOf<Int>() }

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
                .padding(24.dp)
        ) {
            Text("Dodaj nową grupę", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = groupName,
                onValueChange = { groupName = it },
                label = { Text("Nazwa grupy") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Dodaj użytkowników do grupy", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = { navController.navigate("add_user") }) {
                    Icon(Icons.Default.PersonAdd, contentDescription = "Dodaj nowego użytkownika")
                }
            }

            LazyColumn {
                items(users) { user: UserEntity ->
                    val isSelected = user.id in selectedUserIds
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = {
                                if (isSelected) selectedUserIds.remove(user.id)
                                else selectedUserIds.add(user.id)
                            }
                        )
                        Text(user.name)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (groupName.isNotBlank()) {
                        val newGroup = GroupEntity(name = groupName)
                        groupViewModel.addGroup(newGroup) { groupId ->
                            selectedUserIds.forEach { userId ->
                                groupUserCrossRefViewModel.addUserToGroup(groupId, userId)
                            }
                        }
                        Toast.makeText(context, "Dodano grupę: $groupName", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Podaj nazwę grupy", Toast.LENGTH_SHORT).show()
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
