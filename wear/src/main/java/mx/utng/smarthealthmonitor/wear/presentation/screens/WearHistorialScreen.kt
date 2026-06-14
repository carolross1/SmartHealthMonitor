package mx.utng.smarthealthmonitor.wear.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.foundation.rotary.RotaryScrollableDefaults
import androidx.wear.compose.foundation.rotary.rotaryScrollable
import androidx.wear.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import mx.utng.smarthealthmonitor.wear.presentation.WearDashboardViewModel
import mx.utng.smarthealthmonitor.wear.presentation.components.WearFilaHistorial

/**
 * Pantalla de historial de lecturas de FC con soporte de Rotary Input
 * (corona del reloj) mediante .rotaryScrollable().
 */
@Composable
fun WearHistorialScreen(
    onBack: () -> Unit = {},
    viewModel: WearDashboardViewModel = viewModel()
) {
    val historial by viewModel.historial.collectAsState()
    val listState = rememberScalingLazyListState()
    val focusRequester = remember { FocusRequester() }

    // Pedir foco para recibir eventos de la corona
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

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
            modifier = Modifier
                .fillMaxSize()
                .rotaryScrollable( // conecta la corona
                    behavior = RotaryScrollableDefaults.behavior(
                        scrollableState = listState
                    ),
                    focusRequester = focusRequester
                )
        ) {
            item {
                Text(
                    "Historial (${historial.size})",
                    style = MaterialTheme.typography.title3,
                    modifier = Modifier.padding(8.dp)
                )
            }

            if (historial.isEmpty()) {
                item {
                    Text(
                        "Sin lecturas aún",
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            } else {
                items(historial, key = { it.id }) { lectura ->
                    WearFilaHistorial(lectura = lectura)
                }
            }
        }
    }
}
