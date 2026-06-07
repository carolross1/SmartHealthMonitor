package mx.utng.smarthealthmonitor.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1976D2),
    onPrimary = Color.White,
    secondary = Color(0xFF26C6DA),
    error = Color(0xFFD32F2F),
    surface = Color(0xFFF5F5F5),
    background = Color.White
)

@Composable
fun SmartHealthMonitorTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
