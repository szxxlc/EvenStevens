package pwr.szulc.evenstevens.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pwr.szulc.evenstevens.data.viewmodels.GroupViewModel
import pwr.szulc.evenstevens.data.viewmodels.GroupUserCrossRefViewModel
import pwr.szulc.evenstevens.data.viewmodels.UserViewModel
import pwr.szulc.evenstevens.data.DatabaseProvider.getDatabase
import pwr.szulc.evenstevens.ui.common.AppTopBar

@Composable
fun GroupListScreen(
    navController: NavController,
    viewModel: GroupViewModel,
    userViewModel: UserViewModel,
    groupUserCrossRefViewModel: GroupUserCrossRefViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val db = getDatabase(context)

    val groupList by viewModel.groups.collectAsState(initial = emptyList())
    var expandedGroupId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            AppTopBar(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text("Grupy użytkownika", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(12.dp))

            if (groupList.isEmpty()) {
                Text("Brak grup.")
            } else {
                groupList.forEach { group ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { expandedGroupId = if (expandedGroupId == group.id) null else group.id },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = group.name,
                                style = MaterialTheme.typography.titleMedium
                            )

                            if (expandedGroupId == group.id) {
                                val usersInGroup by groupUserCrossRefViewModel
                                    .getUsersForGroup(group.id)
                                    .collectAsState(initial = emptyList())
                                val allUsers by userViewModel.users.collectAsState(initial = emptyList())

                                val memberNames = usersInGroup.mapNotNull { crossRef ->
                                    allUsers.find { it.id == crossRef.userId }?.name
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Członkowie grupy:")
                                memberNames.forEach { name ->
                                    Text("• $name")
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        navController.navigate("group_screen/${group.id}")
                                    },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text("Przejdź do grupy")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate("add_group")
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Filled.AddCircle, contentDescription = "Dodaj nową grupę")
                Spacer(Modifier.width(8.dp))
                Text("Dodaj nową grupę")
            }
        }
    }
}
