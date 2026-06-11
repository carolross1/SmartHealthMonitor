package mx.utng.smarthealthmonitor.wear.presentation.screens

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import mx.utng.smarthealthmonitor.wear.presentation.WearDashboardViewModel
import mx.utng.smarthealthmonitor.wear.presentation.components.WearFCCard
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth

@Composable
fun WearDashboardScreen(
    onAlertClick: () -> Unit = {},
    viewModel: WearDashboardViewModel = viewModel()
) {
    val fc by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = {
            TimeText(modifier = Modifier.scrollAway(listState))
        },
        positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
    ) {
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            // Item 1: Card de FC
            item {
                WearFCCard(
                    fc = fc,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // RETO: CompactChip de pasos
            item {
                CompactChip(
                    label = {
                        Text(
                            text = if (pasos > 0) "🚶 $pasos pasos" else "🚶 -- pasos",
                            maxLines = 1
                        )
                    },
                    onClick = { },
                    colors = ChipDefaults.primaryChipColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Item 3: Chip de Alerta
            item {
                Chip(
                    label = { Text("⚠️ Alerta de emergencia") },
                    onClick = onAlertClick,
                    colors = ChipDefaults.primaryChipColors(
                        backgroundColor = MaterialTheme.colors.error
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}