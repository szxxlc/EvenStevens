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
import kotlinx.coroutines.launch
import pwr.szulc.evenstevens.data.entities.GroupEntity
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
    onToggleTheme: () -> Unit,
    editingGroupId: Int? = null
) {
    val context = LocalContext.current
    val users by userViewModel.users.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    val groupToEditState: State<GroupEntity?> = if (editingGroupId != null) {
        groupViewModel.getGroupById(editingGroupId).collectAsState(initial = null)
    } else {
        remember { mutableStateOf(null) }
    }
    val groupToEdit = groupToEditState.value

    var groupName by remember { mutableStateOf("") }
    val selectedUserIds = remember { mutableStateListOf<Int>() }
    val lockedUserIds = remember { mutableStateListOf<Int>() }

    // Zainicjalizuj dane tylko gdy dostępne są zarówno grupa jak i użytkownicy
    LaunchedEffect(groupToEdit, users) {
        if (groupToEdit != null && users.isNotEmpty()) {
            groupName = groupToEdit.name // ustaw domyślną nazwę
            selectedUserIds.clear()
            lockedUserIds.clear()

            val existingUsers = groupUserCrossRefViewModel.getUsersForGroupSync(groupToEdit.id)
            selectedUserIds.addAll(existingUsers.map { it.id })

            for (user in existingUsers) {
                val cannotRemove = !groupUserCrossRefViewModel.canUserBeRemovedFromGroupSuspend(user.id, groupToEdit.id)
                if (cannotRemove) {
                    lockedUserIds.add(user.id)
                }
            }
        }
    }

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
            Text(
                text = if (groupToEdit != null) "Edytuj grupę" else "Dodaj nową grupę",
                style = MaterialTheme.typography.headlineSmall
            )
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
                Text("Wybierz użytkowników", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = { navController.navigate("add_user") }) {
                    Icon(Icons.Default.PersonAdd, contentDescription = "Dodaj nowego użytkownika")
                }
            }

            LazyColumn {
                items(users) { user ->
                    val isSelected = user.id in selectedUserIds
                    val isLocked = user.id in lockedUserIds
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = {
                                if (isLocked && isSelected) {
                                    Toast.makeText(
                                        context,
                                        "Nie można usunąć użytkownika – ma powiązane wydatki w tej grupie",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    if (isSelected) selectedUserIds.remove(user.id)
                                    else selectedUserIds.add(user.id)
                                }
                            },
                            enabled = !isLocked || isSelected
                        )
                        Text(user.name)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (groupName.isNotBlank()) {
                        coroutineScope.launch {
                            if (groupToEdit != null) {
                                val updatedGroup = groupToEdit.copy(name = groupName)
                                groupViewModel.updateGroup(updatedGroup)

                                val currentUsers = groupUserCrossRefViewModel.getUsersForGroupSync(groupToEdit.id)
                                val currentUserIds = currentUsers.map { it.id }

                                val usersToRemove = currentUserIds
                                    .filter { it !in selectedUserIds && it !in lockedUserIds }

                                usersToRemove.forEach {
                                    groupUserCrossRefViewModel.removeUserFromGroup(groupToEdit.id, it)
                                }

                                selectedUserIds
                                    .filter { it !in currentUserIds }
                                    .forEach { userId ->
                                        groupUserCrossRefViewModel.addUserToGroup(groupToEdit.id, userId)
                                    }

                                Toast.makeText(context, "Zaktualizowano grupę", Toast.LENGTH_SHORT).show()
                            } else {
                                val newGroup = GroupEntity(name = groupName)
                                groupViewModel.addGroup(newGroup) { groupId ->
                                    selectedUserIds.forEach { userId ->
                                        groupUserCrossRefViewModel.addUserToGroup(groupId, userId)
                                    }
                                }
                                Toast.makeText(context, "Dodano grupę: $groupName", Toast.LENGTH_SHORT).show()
                            }
                            navController.popBackStack()
                        }
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
