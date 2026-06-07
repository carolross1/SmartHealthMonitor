package mx.utng.smarthealthmonitor

import android.util.Log
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import mx.utng.smarthealthmonitor.data.SmartHealthRepository

class WearDataReceiver : WearableListenerService() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    companion object {
        const val PATH_HEART_RATE = "/heart_rate"
        const val KEY_BPM = "bpm"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("WearDataReceiver", "Servicio iniciado")
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Log.d("WearDataReceiver", "onDataChanged llamado")
        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED &&
                event.dataItem.uri.path == PATH_HEART_RATE) {
                val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                val bpm = dataMap.getInt(KEY_BPM)
                Log.d("WearDataReceiver", "FC recibida: $bpm bpm")
                scope.launch {
                    SmartHealthRepository.actualizarFC(bpm)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}