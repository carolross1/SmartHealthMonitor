package mx.utng.smarthealthmonitor.wear.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.compose.ui.Modifier
import mx.utng.smarthealthmonitor.data.db.LecturaFC

/**
 * Fila individual del historial de lecturas de FC.
 * Muestra el valor en bpm y la hora de la lectura, con color
 * segun si el valor esta dentro del rango normal.
 */
@Composable
fun WearFilaHistorial(lectura: LecturaFC) {
    val color = if (lectura.esNormal)
        MaterialTheme.colors.primary
    else
        MaterialTheme.colors.error

    Chip(
        label = {
            Text(
                "${lectura.valorBpm} bpm",
                color = color
            )
        },
        secondaryLabel = { Text(lectura.hora) },
        onClick = { },
        colors = ChipDefaults.secondaryChipColors(),
        modifier = Modifier.fillMaxWidth()
    )
}
