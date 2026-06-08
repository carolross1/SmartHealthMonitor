package mx.utng.smarthealthmonitor

import android.util.Log
import com.google.android.gms.wearable.MessageEvent
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
        const val PATH_FC = "/smarthealthmonitor/fc"
        private const val TAG = "WearListener"
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        val data = String(messageEvent.data)
        val path = messageEvent.path
        Log.i(TAG, "Mensaje recibido: path=$path, data=$data")

        if (path == PATH_FC) {
            val bpm = data.toIntOrNull() ?: return
            Log.i(TAG, "BPM actualizado: $bpm")
            scope.launch { SmartHealthRepository.actualizarFC(bpm) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}