package pwr.szulc.evenstevens.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pwr.szulc.evenstevens.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {

    val logo = if (isDarkTheme) {
        painterResource(id = R.drawable.logo_dark)
    } else {
        painterResource(id = R.drawable.logo_light)
    }

        TopAppBar(
        title = {
            Image(
                painter = logo,
                contentDescription = "Logo aplikacji",
                modifier = Modifier.height(40.dp)
            )
        },
        actions = {
            IconButton(onClick = onToggleTheme) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Zmień motyw"
                )
            }
        }
    )
}

