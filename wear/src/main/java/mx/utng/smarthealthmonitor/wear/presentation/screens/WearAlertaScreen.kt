package mx.utng.smarthealthmonitor.wear.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*

@Composable
fun WearAlertaScreen(
    fc: Int,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Text(
            text = "❤️ $fc bpm",
            style = MaterialTheme.typography.title3,
            color = MaterialTheme.colors.error
        )
        Text(
            text = "¿Enviar alerta?",
            style = MaterialTheme.typography.body2
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onConfirmar,
                modifier = Modifier.size(56.dp),
                colors = ButtonDefaults.primaryButtonColors(
                    backgroundColor = MaterialTheme.colors.error
                )
            ) {
                Text("✓", fontSize = 24.sp)
            }

            Button(
                onClick = onCancelar,
                modifier = Modifier.size(56.dp)
            ) {
                Text("✗", fontSize = 24.sp)
            }
        }
    }
}