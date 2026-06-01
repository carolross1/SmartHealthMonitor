package mx.utng.smarthealthmonitor2.data

import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService

class WearListenerService : WearableListenerService() {

    companion object {
        const val PATH_FC = "/smarthealthmonitor2/fc"
        const val PATH_PASOS = "/smarthealthmonitor2/pasos"
        private const val TAG = "WearListener"
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        val data = String(messageEvent.data)
        val path = messageEvent.path
        Log.d(TAG, "Mensaje recibido: path=$path, data=$data")

        when (path) {
            PATH_FC -> {
                val bpm = data.toIntOrNull()
                if (bpm != null) {
                    SmartHealthRepository.actualizarFC(bpm)
                    Log.d(TAG, "FC actualizada: $bpm")
                }
            }
            PATH_PASOS -> {
                val pasos = data.toIntOrNull()
                if (pasos != null) {
                    SmartHealthRepository.actualizarPasos(pasos)
                    Log.d(TAG, "Pasos actualizados: $pasos")
                }
            }
            else -> Log.w(TAG, "Path desconocido: $path")
        }
    }
}