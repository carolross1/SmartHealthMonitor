package mx.utng.smarthealthmonitor.wear.mqtt

import kotlinx.serialization.Serializable

@Serializable
data class FcMessage(
    val bpm: Int,
    val estado: String,
    val timestamp: Long = System.currentTimeMillis()
)
