package mx.utng.smarthealthmonitor.mqtt

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Corre en el teléfono. Se suscribe al topic de FC que publica el reloj
 * (Wear OS), actualiza el SmartHealthRepository (StateFlow + Room, mismo
 * flujo que ya usaba WearDataReceiver) y re-publica un mensaje enriquecido
 * al topic de la TV.
 */
class MqttAppService(private val context: Context) {

    private var client: MqttAsyncClient? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        private const val TAG = "MQTT_APP"
    }

    fun connect() {
        scope.launch {
            client = MqttAsyncClient(MqttConfig.BROKER_URL, MqttConfig.CLIENT_APP, MemoryPersistence())

            val options = MqttConnectOptions().apply {
                userName = MqttConfig.USERNAME
                password = MqttConfig.PASSWORD.toCharArray()
                isCleanSession = true
                connectionTimeout = 30
                keepAliveInterval = 60
                socketFactory = javax.net.ssl.SSLSocketFactory.getDefault()
            }

            client?.setCallback(object : MqttCallback {
                override fun messageArrived(topic: String, message: MqttMessage) {
                    when (topic) {
                        MqttConfig.TOPIC_FC -> handleFcMessage(message)
                    }
                }

                override fun connectionLost(cause: Throwable?) {
                    Log.w(TAG, "Conexión perdida: ${cause?.message}")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            })

            client?.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(token: IMqttToken?) {
                    client?.subscribe(MqttConfig.TOPIC_FC, MqttConfig.QOS)
                    Log.d(TAG, "✅ Conectado y suscrito a ${MqttConfig.TOPIC_FC}")
                }

                override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                    Log.e(TAG, "❌ Error: ${ex?.message}")
                }
            })
        }
    }

    private fun handleFcMessage(msg: MqttMessage) {
        val fcMsg = Json.decodeFromString<FcMessage>(String(msg.payload))

        // 1. Actualizar el Repository (StateFlow + Room del teléfono).
        //    actualizarFC ya hace ambas cosas, igual que en WearDataReceiver.
        scope.launch { SmartHealthRepository.actualizarFC(fcMsg.bpm) }

        // 2. Re-publicar al topic TV con formato enriquecido
        val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val tvMsg = TvMessage(bpm = fcMsg.bpm, estado = fcMsg.estado, hora = hora)
        val tvPayload = Json.encodeToString(tvMsg).toByteArray()
        val tvMqtt = MqttMessage(tvPayload).apply {
            qos = MqttConfig.QOS
            isRetained = true
        }
        client?.publish(MqttConfig.TOPIC_TV, tvMqtt)
        Log.d(TAG, "🔁 Re-publicado al TV: ${fcMsg.bpm} bpm")
    }

    fun disconnect() {
        client?.disconnect()
    }
}
