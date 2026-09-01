package ru.myproject.finhelper.features.screenshot

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.ImageBitmap
import dev.shreyaspatil.capturable.controller.CaptureController
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch

@Composable
fun CapturableScreenWrapper(
    onCapturePerformed:(ImageBitmap?) -> Unit,
    content: @Composable (captureController: CaptureController, triggerCapture: () -> Unit) -> Unit
) {
    val captureController = rememberCaptureController()
    val scope = rememberCoroutineScope ()

    val performCapture: () -> Unit = {
        scope.launch {
            try {
                val bitmap = captureController.captureAsync().await()
                onCapturePerformed(bitmap)
            } catch (throwable: Throwable) {
                Log.e("CapturableScreenWrapper", "Capture failed: ${throwable.message}", throwable)
                onCapturePerformed(null)
            }
        }

    }
    content(captureController,performCapture)
}