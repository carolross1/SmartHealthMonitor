package mx.utng.smarthealthmonitor.wear.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Card
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

@Composable
fun WearFCCard(
    fc: Int,
    modifier: Modifier = Modifier
) {
    val colorFC = if (fc in 60..100)
        MaterialTheme.colors.primary
    else
        MaterialTheme.colors.error

    Card(
        onClick = { },
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "❤️",
                fontSize = 24.sp
            )
            Text(
                text = "$fc",
                style = MaterialTheme.typography.display1,
                color = colorFC,
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp
            )
            Text(
                text = "bpm",
                style = MaterialTheme.typography.caption2,
                color = MaterialTheme.colors.onSurfaceVariant
            )
        }
    }
}