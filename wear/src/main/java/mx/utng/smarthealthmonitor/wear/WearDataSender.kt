package mx.utng.smarthealthmonitor.wear

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await

class WearDataSender(private val context: Context) {

    companion object {
        private const val TAG = "WearDataSender"
        const val PATH_HEART_RATE = "/heart_rate"
        const val KEY_BPM = "bpm"
        const val KEY_TIMESTAMP = "timestamp"
    }

    suspend fun enviarFC(bpm: Int) {
        try {
            val request = PutDataMapRequest.create(PATH_HEART_RATE).apply {
                dataMap.putInt(KEY_BPM, bpm)
                // timestamp fuerza que DataClient detecte el cambio aunque el BPM sea igual
                dataMap.putLong(KEY_TIMESTAMP, System.currentTimeMillis())
            }
            val putDataRequest = request.asPutDataRequest().setUrgent()
            Wearable.getDataClient(context)
                .putDataItem(putDataRequest)
                .await()
            Log.d(TAG, "FC enviada via DataClient: $bpm bpm")
        } catch (e: Exception) {
            Log.e(TAG, "Error enviando FC: ${e.message}")
        }
    }
}