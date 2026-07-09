package mx.utng.smarthealthmonitor.tv.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.tv.domain.model.TvUiState

// SmartHealthRepository es un `object` (singleton), igual que en DashboardViewModel
// (app) y WearDashboardViewModel (wear) — no requiere Factory ni inyección de
// constructor, se referencia directo.
class TvViewModel : ViewModel() {

    private val _state = MutableStateFlow(TvUiState())
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    init {
        // Observar historial reactivo del Room DAO
        viewModelScope.launch {
            SmartHealthRepository.obtenerHistorial()
                .catch { e -> _state.update { it.copy(error = e.message, isLoading = false) } }
                .collect { lecturas ->
                    _state.update { it.copy(lecturas = lecturas, isLoading = false) }
                }
        }

        // Observar FC actual (StateFlow del sensor)
        viewModelScope.launch {
            SmartHealthRepository.fcFlow.collect { bpm ->
                _state.update { it.copy(fcActual = bpm) }
            }
        }
    }
}
