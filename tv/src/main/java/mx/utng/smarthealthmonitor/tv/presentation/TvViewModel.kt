package mx.utng.smarthealthmonitor.tv.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.tv.domain.model.TvUiState
import mx.utng.smarthealthmonitor.tv.mqtt.MqttTvSubscriber
import mx.utng.smarthealthmonitor.tv.mqtt.TvMessage

// SmartHealthRepository es un `object` (singleton), igual que en DashboardViewModel
// (app) y WearDashboardViewModel (wear) — no requiere Factory ni inyección de
// constructor, se referencia directo.
//
// Ahora extiende AndroidViewModel para tener un Context (Application) y poder
// crear el MqttTvSubscriber (Sesión 13). `viewModel()` en Compose soporta esto
// automáticamente porque ComponentActivity ya usa un factory que sabe construir
// AndroidViewModel — no hace falta una Factory propia.
class TvViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(TvUiState())
    val state: StateFlow<TvUiState> = _state.asStateFlow()

    // MQTT: FC en tiempo real reenviada por el teléfono vía HiveMQ Cloud
    private val mqttFlow = MutableStateFlow<TvMessage?>(null)
    private val mqttSubscriber = MqttTvSubscriber(application, mqttFlow)

    init {
        // Observar historial reactivo del Room DAO (local de la TV)
        viewModelScope.launch {
            SmartHealthRepository.obtenerHistorial()
                .catch { e -> _state.update { it.copy(error = e.message, isLoading = false) } }
                .collect { lecturas ->
                    _state.update { it.copy(lecturas = lecturas, isLoading = false) }
                }
        }

        // Observar FC actual (StateFlow local del Repository)
        viewModelScope.launch {
            SmartHealthRepository.fcFlow.collect { bpm ->
                _state.update { it.copy(fcActual = bpm) }
            }
        }

        // MQTT (Sesión 13): conectar y suscribirse al topic TV
        mqttSubscriber.connect()
        viewModelScope.launch {
            mqttFlow.collect { tvMsg ->
                tvMsg ?: return@collect
                _state.update {
                    it.copy(
                        fcActual = tvMsg.bpm,
                        fcEstado = tvMsg.estado,
                        ultimaHora = tvMsg.hora,
                        isLoading = false
                    )
                }
                // Persistir también en el Room local de la TV, así el
                // historial de la fila 2 se llena en tiempo real.
                SmartHealthRepository.actualizarFC(tvMsg.bpm)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mqttSubscriber.disconnect()
    }
}
