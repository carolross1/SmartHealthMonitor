package mx.utng.smarthealthmonitor.tv.mqtt

import mx.utng.smarthealthmonitor.tv.BuildConfig

/**
 * Configuración del broker MQTT (HiveMQ Cloud) para el módulo tv.
 * Duplicado respecto a app/wear por el mismo motivo que SmartHealthRepository:
 * tv es un módulo hermano independiente, sin project(":app").
 */
object MqttConfig {

    const val BROKER_URL: String = BuildConfig.HIVEMQ_BROKER_URL
    const val USERNAME: String = BuildConfig.HIVEMQ_USERNAME
    const val PASSWORD: String = BuildConfig.HIVEMQ_PASSWORD

    const val TOPIC_TV = "utng/smarthealthmonitor/tv"

    const val QOS = 1
    const val CLIENT_TV = "smarthealthmonitor-tv"
}
