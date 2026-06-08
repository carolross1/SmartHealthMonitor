package mx.utng.smarthealthmonitor.wear

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class WearDataSender(private val context: Context) {

    companion object {
        private const val TAG = "WearDataSender"
        const val PATH_FC = "/smarthealthmonitor/fc"
    }

    suspend fun enviarFC(bpm: Int) {
        withContext(Dispatchers.IO) {
            try {
                val nodeClient = Wearable.getNodeClient(context)
                val messageClient = Wearable.getMessageClient(context)
                val data = bpm.toString().toByteArray(Charsets.UTF_8)

                val nodes = nodeClient.connectedNodes.await()
                Log.i(TAG, "Nodos conectados: ${nodes.size}")

                if (nodes.isEmpty()) {
                    Log.w(TAG, "Sin nodos conectados")
                    return@withContext
                }

                for (node in nodes) {
                    messageClient.sendMessage(node.id, PATH_FC, data).await()
                    Log.i(TAG, "FC enviada a ${node.displayName}: $bpm bpm")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}")
            }
        }
    }
}