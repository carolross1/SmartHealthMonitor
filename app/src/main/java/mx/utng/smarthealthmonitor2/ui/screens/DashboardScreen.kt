package mx.utng.smarthealthmonitor2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.utng.smarthealthmonitor2.BuildConfig
import mx.utng.smarthealthmonitor2.data.SmartHealthRepository
import mx.utng.smarthealthmonitor2.data.models.LecturaFC
import mx.utng.smarthealthmonitor2.ui.components.FilaHistorial
import mx.utng.smarthealthmonitor2.ui.components.TarjetaDato
import mx.utng.smarthealthmonitor2.ui.theme.SmartHealthMonitor2Theme
import mx.utng.smarthealthmonitor2.ui.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onHistorialClick: () -> Unit = {},
    onAlertClick: () -> Unit = {},
    viewModel: DashboardViewModel = viewModel()
) {
    val fc by viewModel.fc.collectAsState()
    val pasos by viewModel.pasos.collectAsState()
    val spO2 by viewModel.spO2.collectAsState()
    val historial = viewModel.historial

    SmartHealthMonitor2Theme(dynamicColor = false) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "SmartHealth",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAlertClick,
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Enviar alerta de emergencia",
                        tint = MaterialTheme.colorScheme.onError
                    )
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tarjeta de FC
                item {
                    TarjetaDato(
                        valor = "$fc",
                        unidad = "bpm",
                        label = "Frecuencia cardíaca",
                        colorValor = MaterialTheme.colorScheme.error
                    )
                }

                // Tarjeta de Pasos
                item {
                    TarjetaDato(
                        valor = "%,d".format(pasos),
                        unidad = "pasos",
                        label = "Pasos del día",
                        colorValor = MaterialTheme.colorScheme.primary
                    )
                }

                item {
                    TarjetaDato(
                        valor = "$spO2",
                        unidad = "%",
                        label = "Saturación de Oxígeno",
                        colorValor = MaterialTheme.colorScheme.tertiary
                    )
                }

                // Título del historial
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Historial reciente",
                            style = MaterialTheme.typography.titleMedium
                        )
                        TextButton(onClick = onHistorialClick) {
                            Text("Ver todo")
                        }
                    }
                }

                // Lista de historial
                items(historial, key = { it.id }) { lectura ->
                    FilaHistorial(lectura = lectura)
                }

                // Botón de simulación (actualizado con SpO2)
                item {
                    if (BuildConfig.DEBUG) {
                        OutlinedButton(
                            onClick = {
                                val fcSimulado = (60..110).random()
                                val pasosSimulado = (3000..8000).random()
                                val spo2Simulado = (88..100).random()
                                SmartHealthRepository.actualizarFC(fcSimulado)
                                SmartHealthRepository.actualizarPasos(pasosSimulado)
                                SmartHealthRepository.actualizarSpO2(spo2Simulado)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("🎮 Simular dato del wearable (DEBUG)")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Dashboard - Light",
    showSystemUi = true, apiLevel = 33)
@Preview(showBackground = true, name = "Dashboard - Dark",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, apiLevel = 33)
@Composable
private fun DashboardScreenPreview() {
    SmartHealthMonitor2Theme(dynamicColor = false) {
        DashboardScreen()
    }
}