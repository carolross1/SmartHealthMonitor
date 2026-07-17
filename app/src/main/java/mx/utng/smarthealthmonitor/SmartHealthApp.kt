package mx.utng.smarthealthmonitor

import android.app.Application
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.mqtt.MqttAppService

class SmartHealthApp : Application() {

    lateinit var mqttService: MqttAppService
        private set

    override fun onCreate() {
        super.onCreate()
        SmartHealthRepository.init(this)

        // MQTT (Sesión 13): recibe FC del reloj vía HiveMQ Cloud y
        // re-publica al topic de la TV.
        mqttService = MqttAppService(this)
        mqttService.connect()
    }
}