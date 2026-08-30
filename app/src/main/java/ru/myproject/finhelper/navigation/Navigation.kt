package ru.myproject.finhelper.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import ru.myproject.finhelper.ui.appBar.BottomAppBarHome
import ru.myproject.finhelper.activity.WelcomeContent
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.myproject.finhelper.repository.FinRepositoryImpl
import ru.myproject.finhelper.ui.screenshot.ScreenShot
import ru.myproject.finhelper.ui.appBar.TopAppBarBack
import ru.myproject.finhelper.ui.taxes.income_tax.ShowPersonalTax
import ru.myproject.finhelper.ui.taxes.property_tax.ShowPropertyTax
import ru.myproject.finhelper.ui.taxes.vat.ShowVATAdded
import ru.myproject.finhelper.viewmodel.FinViewModel
import ru.myproject.finhelper.viewmodel.FinViewModelFactory


@SuppressLint("SuspiciousIndentation")
@Composable
fun MyAppNavGraph() {

    var showBottomBar by rememberSaveable() { mutableStateOf(false) }
    val navController = rememberNavController()

    /*Для TopBar
    Позволяет следить за текущим стеком навигации.
    При изменении параметра route Compose может перерисовывать интерфейс
     */
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        var screenShotTriggerCapture: (() -> Unit)? by remember { mutableStateOf(null) }
    //Добавлена viewModel
    val finViewModel: FinViewModel = viewModel(factory = FinViewModelFactory(FinRepositoryImpl()))

        Scaffold(
            topBar = {TopAppBarBack(navController = navController,
                currentRoute = currentRoute,
                onCaptureClick = {screenShotTriggerCapture?.invoke()} )
                     },
            bottomBar = { if (showBottomBar) BottomAppBarHome(navController = navController) }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "HomeScreenUI",
                modifier = Modifier.padding(innerPadding))

                {
                    composable("HomeScreenUI") {
                        WelcomeContent(
                            onShowBottomBar = {show -> showBottomBar = show},
                            navController = navController)
                    }
                    composable("TaxesUI") {
                        TaxesChoice(
                            onShowBottomBar = {show -> showBottomBar = show},
                            navController = navController)
                    }
                    composable("VATCalc_UI") {
                        ScreenShot(navController = navController, currentRoute = currentRoute,
                            onShowBottomBar = {show -> showBottomBar = show},
                            onTriggerCaptureReady = { trigger -> screenShotTriggerCapture = trigger },

                            { modifier -> // contentToCapture - здесь мы его определяем
                                ShowVATAdded(
                                    onShowBottomBar = {show -> showBottomBar = show},
                                    modifier = modifier,
                                    finViewModel = finViewModel // Передаем finViewModel, как обычно
                                )
                            }

                        )

                    }
                    composable("PersonalTaxCalc_UI") {
                        ScreenShot(navController = navController, currentRoute = currentRoute,
                            onShowBottomBar = {show -> showBottomBar = show},
                            onTriggerCaptureReady = { trigger -> screenShotTriggerCapture = trigger },

                            { modifier ->
                                ShowPersonalTax(
                                    onShowBottomBar = {show -> showBottomBar = show},
                                    modifier = modifier,
                                    finViewModel = finViewModel
                                )
                            }

                        )

                    }

                    composable("PropertyTaxCalc") {
                        ScreenShot(navController = navController, currentRoute = currentRoute,
                            onShowBottomBar = {show -> showBottomBar = show},
                            onTriggerCaptureReady = { trigger -> screenShotTriggerCapture = trigger },

                            { modifier ->
                                ShowPropertyTax(
                                    onShowBottomBar = {show -> showBottomBar = show},
                                    modifier = modifier,
                                    finViewModel = finViewModel
                                )
                            }

                        )

                    }
                }
        }
}