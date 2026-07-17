package mx.utng.smarthealthmonitor.tv.mqtt

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

/**
 * Se suscribe al topic de TV y expone cada mensaje recibido a través de
 * tvFlow para que el TvViewModel actualice el TvUiState en tiempo real.
 */
class MqttTvSubscriber(
    private val context: Context,
    private val tvFlow: MutableStateFlow<TvMessage?>
) {
    private var client: MqttAsyncClient? = null

    companion object {
        private const val TAG = "MQTT_TV"
    }

    fun connect() {
        client = MqttAsyncClient(MqttConfig.BROKER_URL, MqttConfig.CLIENT_TV, MemoryPersistence())

        client?.setCallback(object : MqttCallback {
            override fun messageArrived(topic: String, msg: MqttMessage) {
                if (topic == MqttConfig.TOPIC_TV) {
                    val tvMsg = Json.decodeFromString<TvMessage>(String(msg.payload))
                    tvFlow.value = tvMsg
                    Log.d(TAG, "📺 Recibido: ${tvMsg.bpm} bpm")
                }
            }

            override fun connectionLost(cause: Throwable?) {
                Log.w(TAG, "Conexión perdida: ${cause?.message}")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {}
        })

        val options = MqttConnectOptions().apply {
            userName = MqttConfig.USERNAME
            password = MqttConfig.PASSWORD.toCharArray()
            isCleanSession = true
            socketFactory = javax.net.ssl.SSLSocketFactory.getDefault()
        }

        client?.connect(options, null, object : IMqttActionListener {
            override fun onSuccess(token: IMqttToken?) {
                client?.subscribe(MqttConfig.TOPIC_TV, MqttConfig.QOS)
                Log.d(TAG, "✅ TV suscrita a ${MqttConfig.TOPIC_TV}")
            }

            override fun onFailure(token: IMqttToken?, ex: Throwable?) {
                Log.e(TAG, "❌ Error: ${ex?.message}")
            }
        })
    }

    fun disconnect() {
        client?.disconnect()
    }
}
