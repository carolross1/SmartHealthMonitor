package mx.utng.smarthealthmonitor2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.utng.smarthealthmonitor2.navigation.SmartHealthNavGraph
import mx.utng.smarthealthmonitor2.ui.theme.SmartHealthMonitor2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHealthNavGraph()
        }
    }
        }


// Función para la pantalla principal (opcional)
@Composable
fun SmartHealthMonitorScreen() {
    Text(
        text = "SmartHealth Monitor",
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(24.dp)
    )
}

// Previews del tema
@Preview(showBackground = true, name = "Light")
@Preview(
    showBackground = true,
    name = "Dark",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ThemePreview() {
    SmartHealthMonitor2Theme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "SmartHealth Monitor",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}