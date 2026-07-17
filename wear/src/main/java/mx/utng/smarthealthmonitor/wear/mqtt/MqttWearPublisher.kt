package mx.utng.smarthealthmonitor.wear.mqtt

import android.content.Context
import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

/**
 * Publica la FC leída del sensor (Health Services) al broker MQTT.
 * Se usa junto con WearDataSender: WearDataSender sigue enviando por
 * Wearable Data Layer directo al teléfono emparejado, y este publisher
 * además lo manda por MQTT (funciona sin emparejamiento BLE, solo con
 * la red del emulador).
 */
class MqttWearPublisher(private val context: Context) {

    private var client: MqttAsyncClient? = null

    companion object {
        private const val TAG = "MQTT_WEAR"
    }

    fun connect() {
        client = MqttAsyncClient(MqttConfig.BROKER_URL, MqttConfig.CLIENT_WEAR, MemoryPersistence())

        val options = MqttConnectOptions().apply {
            userName = MqttConfig.USERNAME
            password = MqttConfig.PASSWORD.toCharArray()
            isCleanSession = true
            connectionTimeout = 30
            keepAliveInterval = 60
            // SSL habilitado automáticamente por la URL ssl://
            socketFactory = javax.net.ssl.SSLSocketFactory.getDefault()
        }

        client?.connect(options, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d(TAG, "✅ Conectado a HiveMQ Cloud")
            }

            override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                Log.e(TAG, "❌ Error: ${ex?.message}")
            }
        })
    }

    /** Publicar FC al topic MQTT */
    fun publishFC(bpm: Int, estado: String) {
        if (client?.isConnected != true) return

        val message = FcMessage(bpm = bpm, estado = estado)
        val payload = Json.encodeToString(message).toByteArray()
        val mqttMessage = MqttMessage(payload).apply {
            qos = MqttConfig.QOS
            isRetained = true // el TV verá el último valor al conectarse
        }
        client?.publish(MqttConfig.TOPIC_FC, mqttMessage)
        Log.d(TAG, "📤 Publicado: $bpm bpm → ${MqttConfig.TOPIC_FC}")
    }

    fun disconnect() {
        client?.disconnect()
    }
}
