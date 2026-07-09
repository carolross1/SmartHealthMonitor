package mx.utng.smarthealthmonitor.tv.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.TvLazyRow
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

@Composable
fun TvCatalogScreen(
    viewModel: TvViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(Modifier.fillMaxSize().background(Color(0xFF0D1B4A))) {
        if (state.isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
            return@Box
        }

        TvLazyColumn(
            modifier = Modifier.fillMaxSize().padding(48.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Fila 1: FC actual
            item {
                RowSection(title = "⚡ Estado Actual — ${state.fcActual} bpm") {
                    TvLazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(state.lecturas.takeLast(3)) { lectura ->
                            FcCardItem(lectura = lectura, onClick = {})
                        }
                    }
                }
            }

            // Fila 2: Historial completo
            item {
                RowSection(title = "📋 Historial FC") {
                    TvLazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(state.lecturas) { lectura ->
                            FcCardItem(lectura = lectura, onClick = {})
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowSection(title: String, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            title,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        content()
    }
}
