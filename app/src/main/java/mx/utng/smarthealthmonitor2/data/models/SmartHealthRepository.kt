package mx.utng.smarthealthmonitor2.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SmartHealthRepository {

    private val _fcFlow = MutableStateFlow(0)
    val fcFlow: StateFlow<Int> = _fcFlow.asStateFlow()

    private val _pasosFlow = MutableStateFlow(0)
    val pasosFlow: StateFlow<Int> = _pasosFlow.asStateFlow()

    private val _spo2Flow = MutableStateFlow(0)
    val spo2Flow: StateFlow<Int> = _spo2Flow.asStateFlow()

    fun actualizarFC(bpm: Int) {
        _fcFlow.value = bpm
    }

    fun actualizarPasos(pasos: Int) {
        _pasosFlow.value = pasos
    }

    fun actualizarSpO2(spo2: Int) {
        _spo2Flow.value = spo2
    }
}