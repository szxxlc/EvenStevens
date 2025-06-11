package pwr.szulc.evenstevens.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pwr.szulc.evenstevens.data.entities.GroupEntity
import pwr.szulc.evenstevens.data.viewmodels.GroupViewModel
import pwr.szulc.evenstevens.data.entities.UserEntity
import pwr.szulc.evenstevens.data.viewmodels.UserViewModel
import pwr.szulc.evenstevens.ui.common.AppTopBar

@Composable
fun AddUserScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    var userName by remember { mutableStateOf("") }

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
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text("Dodaj nowego użytkownika", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Nazwa użytkownika") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (userName.isNotBlank()) {
                        userViewModel.addUser(UserEntity(name = userName))
                        Toast.makeText(context, "Dodano użytkownika: $userName", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Podaj nazwę użytkownika", Toast.LENGTH_SHORT).show()
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
