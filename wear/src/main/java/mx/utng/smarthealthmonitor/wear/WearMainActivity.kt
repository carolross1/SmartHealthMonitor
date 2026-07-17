package mx.utng.smarthealthmonitor.wear

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import mx.utng.smarthealthmonitor.wear.presentation.WearDashboardViewModel
import mx.utng.smarthealthmonitor.wear.presentation.navigation.SmartHealthWearNavGraph
import mx.utng.smarthealthmonitor.wear.presentation.theme.SmartHealthWearTheme

class WearMainActivity : ComponentActivity() {

    private lateinit var wearDataSender: WearDataSender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wearDataSender = WearDataSender(this)

        // Inicializar el Repository (DAO de Room) para el historial
        SmartHealthRepository.init(this)

        // Registrar HealthDataService como PassiveListener de Health Services.
        // Sin esto, el servicio nunca se activa y no llegan datos reales del
        // sensor (ni por Wearable Data Layer ni por MQTT).
        lifecycleScope.launch {
            HealthDataService.registrar(this@WearMainActivity)
        }

        // Limpiar todos los items anteriores del DataLayer
        lifecycleScope.launch {
            try {
                Wearable.getDataClient(this@WearMainActivity)
                    .deleteDataItems(
                        Uri.parse("wear://*/heart_rate"),
                        DataClient.FILTER_PREFIX
                    ).await()
                Log.d("WearMainActivity", "DataLayer limpiado")
            } catch (e: Exception) {
                Log.e("WearMainActivity", "Error limpiando: ${e.message}")
            }
        }

        setContent {
            SmartHealthWearTheme {
                SmartHealthWearNavGraph()
            }
        }
    }
}