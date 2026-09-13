package ru.myproject.finhelper.features.screenshot

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import dev.shreyaspatil.capturable.capturable
import androidx.compose.ui.graphics.asAndroidBitmap
import ru.myproject.finhelper.R

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScreenShot(
    navController: NavController,
    currentRoute: String?,
    onShowBottomBar: (Boolean) -> Unit,
    onTriggerCaptureReady: (() -> Unit) -> Unit,
    contentToCapture: @Composable (Modifier) -> Unit){

    // Получаем контекст для сохранения/шаринга
    val context = LocalContext.current
    // Состояние для хранения захваченного скриншота и отображения диалога
    var capturedBitmap: ImageBitmap? by remember { mutableStateOf(null) }
    var showActionDialog by remember{ mutableStateOf(false) }

    CapturableScreenWrapper(
        onCapturePerformed = { bitmap ->
            if (bitmap != null) {
                capturedBitmap = bitmap
                showActionDialog = true
            } else {
                throw Exception()
            }
        }
    ) { captureController, triggerCapture ->
        LaunchedEffect(Unit) {
            onTriggerCaptureReady(triggerCapture)
        }

        Scaffold(
            topBar = { },
            bottomBar = { }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize() // Делаем, чтобы Column занимал весь экран
                    .padding(paddingValues) // Применяем отступы от Scaffold
                    .capturable(captureController) // <-- Применяем модификатор захвата к этому Column
            ) {
                contentToCapture(Modifier.fillMaxSize())
            }
        }
    }
    if(showActionDialog && capturedBitmap != null) {
        ScreenShotActionDialog(
            onDismissRequest = {
                showActionDialog = false
                capturedBitmap = null
            },
            onSaveClick = {
                capturedBitmap?.let{bitmap ->
                    // Вызываем функцию сохранения
                    saveImageToGallery(context, bitmap.asAndroidBitmap(),
                        "VAT_Screenshot_${System.currentTimeMillis()}")
                    Toast.makeText(context,
                        R.string.Screenshot_saved, Toast.LENGTH_SHORT).show()
                }
                showActionDialog = false
                capturedBitmap = null
            },
            onShareClick = {
                capturedBitmap?.let { bitmap ->
                    // Вызываем функцию "поделиться"
                    shareImage(context, bitmap.asAndroidBitmap(),
                        "VAT_Screenshot_${System.currentTimeMillis()}.png")
                }
                showActionDialog = false
                capturedBitmap = null
            }
        )
    }
}