package mx.utng.smarthealthmonitor.wear

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.*
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WearMainActivity : ComponentActivity() {

    private lateinit var wearDataSender: WearDataSender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wearDataSender = WearDataSender(this)

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
            WearApp(
                onEnviarFC = { bpm ->
                    lifecycleScope.launch {
                        wearDataSender.enviarFC(bpm)
                    }
                }
            )
        }
    }
}

@Composable
fun WearApp(onEnviarFC: (Int) -> Unit) {
    var sliderValue by remember { mutableStateOf(0.3f) }
    val bpm = (40f + (sliderValue * 140f)).toInt()
    val esNormal = bpm in 60..100
    val colorBpm = if (esNormal) Color(0xFF4CAF50) else Color(0xFFF44336)
    var enviado by remember { mutableStateOf(false) }

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(text = "❤", fontSize = 24.sp, color = colorBpm)
                Text(
                    text = "$bpm",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorBpm
                )
                Text(text = "bpm", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = if (esNormal) "Normal" else "Fuera de rango",
                    fontSize = 11.sp,
                    color = colorBpm
                )
                InlineSlider(
                    value = sliderValue,
                    onValueChange = {
                        sliderValue = it
                        enviado = false
                    },
                    steps = 70,
                    decreaseIcon = {
                        Icon(
                            imageVector = InlineSliderDefaults.Decrease,
                            contentDescription = "Bajar"
                        )
                    },
                    increaseIcon = {
                        Icon(
                            imageVector = InlineSliderDefaults.Increase,
                            contentDescription = "Subir"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        onEnviarFC(bpm)
                        enviado = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (enviado) Color(0xFF388E3C) else Color(0xFF1976D2)
                    )
                ) {
                    Text(
                        text = if (enviado) "✓ Enviado" else "Enviar al teléfono",
                        fontSize = 11.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}