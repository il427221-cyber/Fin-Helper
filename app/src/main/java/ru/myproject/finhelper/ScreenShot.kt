package ru.myproject.finhelper

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.shreyaspatil.capturable.capturable
import ru.myproject.finhelper.repository.FinRepositoryImpl
import ru.myproject.finhelper.ui.ShowVATAdded
import ru.myproject.finhelper.viewmodel.FinViewModel
import ru.myproject.finhelper.viewmodel.FinViewModelFactory

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScreenShot(
    navController: NavController, currentRoute: String?,
               onShowBottomBar: (Boolean) -> Unit){

    val finViewModel: FinViewModel = viewModel(factory = FinViewModelFactory(FinRepositoryImpl()))

      CapturableScreenWrapper(
        onCapturePerformed = {bitmap ->
            if(bitmap != null) {
                //TODO - Логика сохранения/отправки
            } else {
                throw Exception()
            }
        }
      ) {captureController, triggerCapture ->
          Scaffold(
              topBar = { triggerCapture() },
              bottomBar = { }
          ) {paddingValues ->
              Column(
                  modifier = Modifier
                      .fillMaxSize() // Делаем, чтобы Column занимал весь экран
                      .padding(paddingValues) // Применяем отступы от Scaffold
                      .capturable(captureController) // <-- Применяем модификатор захвата к этому Column
              ) {
                  ShowVATAdded(
                      onShowBottomBar = onShowBottomBar,
                      modifier = Modifier.fillMaxSize(),
                      finViewModel = finViewModel)
              }
          }
      }
}