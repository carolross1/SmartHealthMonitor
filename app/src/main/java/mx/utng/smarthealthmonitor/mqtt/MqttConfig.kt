package mx.utng.smarthealthmonitor.mqtt

import mx.utng.smarthealthmonitor.BuildConfig

/**
 * Configuración del broker MQTT (HiveMQ Cloud).
 *
 * Las credenciales NO están hardcodeadas: se leen desde local.properties
 * (archivo que NO se sube al repositorio, ver .gitignore) a través de
 * BuildConfig — ver el buildConfigField en app/build.gradle.kts.
 */
object MqttConfig {

    const val BROKER_URL: String = BuildConfig.HIVEMQ_BROKER_URL
    const val USERNAME: String = BuildConfig.HIVEMQ_USERNAME
    const val PASSWORD: String = BuildConfig.HIVEMQ_PASSWORD

    // Topics del proyecto (convención UTNG)
    const val TOPIC_FC = "utng/smarthealthmonitor/fc"
    const val TOPIC_TV = "utng/smarthealthmonitor/tv"
    const val TOPIC_ALERT = "utng/smarthealthmonitor/alerta"

    // QoS: 0=best effort, 1=at least once, 2=exactly once
    const val QOS = 1

    // Client IDs únicos por dispositivo
    const val CLIENT_WEAR = "smarthealthmonitor-wear"
    const val CLIENT_APP = "smarthealthmonitor-app"
    const val CLIENT_TV = "smarthealthmonitor-tv"
}
