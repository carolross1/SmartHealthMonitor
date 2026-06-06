package mx.utng.smarthealthmonitor.wear

import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.health.services.client.data.SampleDataPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch

/**
 * Servicio pasivo que escucha el sensor HEART_RATE_BPM del wearable
 * usando Health Services API y lo reenvía al teléfono.
 */
class HealthDataService : PassiveListenerService() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var wearDataSender: WearDataSender

    companion object {
        private const val TAG = "HealthDataService"

        /**
         * Registra este servicio como PassiveListener en Health Services.
         * Llamar desde WearMainActivity en el onCreate().
         */
        suspend fun registrar(context: android.content.Context) {
            try {
                val hsClient = HealthServices.getClient(context)
                val passiveClient = hsClient.passiveMonitoringClient

                val config = PassiveListenerConfig.builder()
                    .setDataTypes(setOf(DataType.HEART_RATE_BPM))
                    .setShouldUserActivityInfoBeRequested(true)
                    .build()

                passiveClient.setPassiveListenerServiceAsync(
                    HealthDataService::class.java,
                    config
                ).await()

                Log.d(TAG, "HealthDataService registrado correctamente")
            } catch (e: Exception) {
                Log.e(TAG, "Error registrando HealthDataService: ${e.message}")
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        wearDataSender = WearDataSender(this)
        Log.d(TAG, "HealthDataService creado")
    }

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        val fcDataPoints = dataPoints.getData(DataType.HEART_RATE_BPM)
        fcDataPoints.forEach { dataPoint ->
            if (dataPoint is SampleDataPoint<Double>) {
                val bpm = dataPoint.value.toInt()
                Log.d(TAG, "FC recibida del sensor: $bpm bpm")
                scope.launch {
                    wearDataSender.enviarFC(bpm)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
