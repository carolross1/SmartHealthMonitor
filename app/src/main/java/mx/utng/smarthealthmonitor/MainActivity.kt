package mx.utng.smarthealthmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.wearable.Wearable
import mx.utng.smarthealthmonitor.navigation.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el canal de comunicación con el wearable
        Wearable.getMessageClient(this)
        Wearable.getNodeClient(this)

        setContent {
            val navController = rememberNavController()
            NavGraph(navController = navController)
        }
    }
}