package pwr.szulc.evenstevens.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pwr.szulc.evenstevens.ui.common.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorInfoScreen(
    navController: NavController,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Spacer(modifier = Modifier.width(0.dp)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Powrót")
                    }
                },
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = "Zmień motyw"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "O projekcie",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("EvenStevens to aplikacja stworzona w ramach projektu na przedmiot Projektowanie Aplikacji Mobilnych. Umożliwia ona zarządzanie podziałem kosztów między użytkownikami w grupie.")
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "O projekcie",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(4.dp))
            Text("Paulina Szulc 272592\nInformatyczne Systemy Automatyki\nPolitechnika Wrocławska")

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                uriHandler.openUri("https://github.com/szxxlc/EvenStevens")
            }) {
                Text("Repozytorium projektu na GitHubie")
            }
        }
    }
}
