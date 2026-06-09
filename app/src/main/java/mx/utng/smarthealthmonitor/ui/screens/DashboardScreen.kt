package mx.utng.smarthealthmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.models.MockData
import mx.utng.smarthealthmonitor.ui.theme.SmartHealthMonitorTheme
import mx.utng.smarthealthmonitor.ui.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onVerHistorial: () -> Unit,
    viewModel: DashboardViewModel = viewModel()
) {
    val fcActual by viewModel.fc.collectAsState()

    // Estado para el diálogo y Snackbar
    var mostrarAlerta by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // RETO: Variable para almacenar la última alerta enviada (para posible deshacer)
    var ultimaAlertaEnviada by remember { mutableStateOf("") }

    // Diálogo condicional
    if (mostrarAlerta) {
        AlertScreen(
            fc = fcActual,
            onDismiss = { mostrarAlerta = false },
            onConfirmar = { nota ->
                mostrarAlerta = false
                ultimaAlertaEnviada = nota  // Guardar la nota para posible deshacer
                scope.launch {
                    // RETO: Snackbar con botón Deshacer
                    val result = snackbarHostState.showSnackbar(
                        message = "✅ Alerta enviada a tus contactos de emergencia",
                        actionLabel = "DESHACER",
                        duration = SnackbarDuration.Long
                    )

                    // RETO: Manejar la acción Deshacer
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            // Mostrar segundo Snackbar confirmando cancelación
                            snackbarHostState.showSnackbar(
                                message = "❌ Alerta cancelada. No se notificó a los contactos.",
                                duration = SnackbarDuration.Short
                            )
                        }
                        SnackbarResult.Dismissed -> {
                            // El Snackbar se cerró normalmente (sin deshacer)
                            // No se requiere acción adicional
                        }
                    }
                }
            }
        )
    }

    SmartHealthMonitorTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("SmartHealth Monitor") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { mostrarAlerta = true },
                    containerColor = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(56.dp)  // 48dp mínimo táctil
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Enviar alerta de emergencia",
                        tint = MaterialTheme.colorScheme.onError
                    )
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Resumen de salud",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item {
                    TarjetaDato(
                        icono = Icons.Default.Favorite,
                        titulo = "Frecuencia Cardiaca",
                        valor = "$fcActual",
                        unidad = "bpm",
                        esNormal = fcActual in 60..100
                    )
                }

                item {
                    TarjetaDato(
                        icono = Icons.Default.WaterDrop,
                        titulo = "SpO₂",
                        valor = "${MockData.spo2Actual}",
                        unidad = "%",
                        esNormal = MockData.spo2Actual >= 95
                    )
                }

                item {
                    TarjetaDato(
                        icono = Icons.Default.DirectionsRun,
                        titulo = "Pasos Hoy",
                        valor = "${MockData.pasosDiarios}",
                        unidad = "pasos",
                        esNormal = true
                    )
                }

                item {
                    Spacer(Modifier.height(8.dp))
                }

                item {
                    Button(
                        onClick = onVerHistorial,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver historial de FC")
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaDato(
    icono: ImageVector,
    titulo: String,
    valor: String,
    unidad: String,
    esNormal: Boolean
) {
    val color = if (esNormal) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.error

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icono,
                contentDescription = titulo,
                tint = color,
                modifier = Modifier.size(36.dp)
            )
            Column {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = valor,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = unidad,
                        style = MaterialTheme.typography.bodyMedium,
                        color = color,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
    }
}