package mx.utng.smarthealthmonitor.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.data.db.LecturaFC

class WearDashboardViewModel : ViewModel() {

    private val _fc = MutableStateFlow(110)
    val fc: StateFlow<Int> = _fc.asStateFlow()

    private val _spo2 = MutableStateFlow(98)
    val spo2: StateFlow<Int> = _spo2.asStateFlow()

    private val _pasos = MutableStateFlow(0)
    val pasos: StateFlow<Int> = _pasos.asStateFlow()

    // NUEVO: historial desde Room
    val historial: StateFlow<List<LecturaFC>> =
        SmartHealthRepository.obtenerHistorial()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    // Llamado desde WearDataListenerService cuando llegan datos del teléfono
    fun updateFc(value: Int) {
        viewModelScope.launch { _fc.emit(value) }
    }

    fun updateSpo2(value: Int) {
        viewModelScope.launch { _spo2.emit(value) }
    }

    fun updatePasos(value: Int) {
        viewModelScope.launch { _pasos.emit(value) }
    }
}
