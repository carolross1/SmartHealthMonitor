package mx.utng.smarthealthmonitor.wear.mqtt

import mx.utng.smarthealthmonitor.wear.BuildConfig

/**
 * Configuración del broker MQTT (HiveMQ Cloud) para el módulo wear.
 *
 * Duplicado respecto al módulo app a propósito: wear y app son módulos
 * hermanos independientes (sin dependencia project(":app")), igual que
 * ya se hace con SmartHealthRepository/Room para evitar los problemas
 * de AAPT entre módulos.
 */
object MqttConfig {

    const val BROKER_URL: String = BuildConfig.HIVEMQ_BROKER_URL
    const val USERNAME: String = BuildConfig.HIVEMQ_USERNAME
    const val PASSWORD: String = BuildConfig.HIVEMQ_PASSWORD

    const val TOPIC_FC = "utng/smarthealthmonitor/fc"

    const val QOS = 1
    const val CLIENT_WEAR = "smarthealthmonitor-wear"
}
