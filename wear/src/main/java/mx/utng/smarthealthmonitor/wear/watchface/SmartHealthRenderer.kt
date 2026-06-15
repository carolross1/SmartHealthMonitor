package mx.utng.smarthealthmonitor.wear.watchface

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.view.SurfaceHolder
import androidx.wear.watchface.CanvasType
import androidx.wear.watchface.ComplicationSlotsManager
import androidx.wear.watchface.DrawMode
import androidx.wear.watchface.Renderer
import androidx.wear.watchface.WatchState
import androidx.wear.watchface.style.CurrentUserStyleRepository
import mx.utng.smarthealthmonitor.data.SmartHealthRepository
import java.time.ZonedDateTime

/**
 * Renderer del WatchFace de SmartHealth: dibuja la hora digital y la FC
 * (frecuencia cardiaca) tomada en tiempo real de [SmartHealthRepository].
 */
class SmartHealthRenderer(
    private val context: Context,
    surfaceHolder: SurfaceHolder,
    watchState: WatchState,
    complicationSlotsManager: ComplicationSlotsManager,
    currentUserStyleRepository: CurrentUserStyleRepository,
    interactiveDrawModeUpdateDelayMillis: Long
) : Renderer.CanvasRenderer2<Renderer.SharedAssets>(
    surfaceHolder, currentUserStyleRepository, watchState,
    CanvasType.HARDWARE, interactiveDrawModeUpdateDelayMillis,
    clearWithBackgroundTintBeforeRenderingHighlightLayer = false
) {

    private val paintHora = Paint().apply {
        color = Color.WHITE
        textSize = 72f
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
    }
    private val paintFC = Paint().apply {
        color = Color.RED
        textSize = 30f
        isAntiAlias = true
    }
    private val paintSub = Paint().apply {
        color = Color.GRAY
        textSize = 22f
        isAntiAlias = true
    }

    // RETO: paints para modo AOD (Always-On Display) - sin antiAlias
    // para ahorrar bateria, solo hora en blanco sobre negro puro.
    private val paintHoraAmbient = Paint().apply {
        color = Color.WHITE
        textSize = 72f
        isAntiAlias = false
        typeface = Typeface.DEFAULT_BOLD
    }

    override suspend fun createSharedAssets(): SharedAssets =
        object : SharedAssets {
            override fun onDestroy() {}
        }

    override fun render(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: SharedAssets
    ) {
        // Fondo negro - ahorra bateria en modo AOD
        canvas.drawColor(Color.BLACK)

        val cx = bounds.exactCenterX()
        val cy = bounds.exactCenterY()

        // RETO: modo AOD (Always-On Display) - solo hora en blanco
        // sobre negro puro, sin FC, con paints sin antiAlias.
        if (renderParameters.drawMode == DrawMode.AMBIENT) {
            val horaAmbient = String.format("%02d:%02d", zonedDateTime.hour, zonedDateTime.minute)
            val twAmbient = paintHoraAmbient.measureText(horaAmbient)
            canvas.drawText(horaAmbient, cx - twAmbient / 2, cy, paintHoraAmbient)
            return
        }

        // Hora digital centrada
        val hora = String.format("%02d:%02d", zonedDateTime.hour, zonedDateTime.minute)
        val tw = paintHora.measureText(hora)
        canvas.drawText(hora, cx - tw / 2, cy - 10f, paintHora)

        // Segundos (pequeno debajo)
        val seg = String.format("%02d", zonedDateTime.second)
        canvas.drawText(seg, cx - 18f, cy + 30f, paintSub)

        // FC desde SmartHealthRepository
        val fc = SmartHealthRepository.fcFlow.value
        if (fc > 0) {
            val fcStr = "❤ $fc bpm"
            val fcW = paintFC.measureText(fcStr)
            canvas.drawText(fcStr, cx - fcW / 2, cy + 70f, paintFC)
        }
    }

    override fun renderHighlightLayer(
        canvas: Canvas,
        bounds: Rect,
        zonedDateTime: ZonedDateTime,
        sharedAssets: SharedAssets
    ) {
        canvas.drawColor(renderParameters.highlightLayer!!.backgroundTint)
    }
}
