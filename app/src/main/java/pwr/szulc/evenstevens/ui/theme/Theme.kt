package pwr.szulc.evenstevens.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF80A102),        // Akcent
    secondary = Color(0xFFFFF6C3),      // Akcent 2
    tertiary = Color(0xFF4E342E),       // np. guziki drugorzędne
    background = Color(0xFF121212),     // Tło główne
    surface = Color(0xFF1E1E1E),        // np. karty, pola tekstowe
    onPrimary = Color(0xFF121212),      // Tekst na primary
    onSurface = Color(0xFFECECEC)       // Tekst na surface
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF7F5EFD),
    secondary = Color(0xFF00093C),
    tertiary = Color(0xFF795548),
    background = Color(0xFFFDFDFD),
    surface = Color(0xFFF1F1F1),
    onPrimary = Color(0xFFFDFDFD),
    onSurface = Color(0xFF1E1E1E)
)

@Composable
fun EvenStevensTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
