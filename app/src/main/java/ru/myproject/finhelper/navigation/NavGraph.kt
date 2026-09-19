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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import ru.myproject.finhelper.features.appBar.BottomAppBarHome
import ru.myproject.finhelper.activity.WelcomeContent
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.myproject.finhelper.features.screenshot.ScreenShot
import ru.myproject.finhelper.features.appBar.TopAppBarBack
import ru.myproject.finhelper.ui.deposit.complex.ShowComplexDeposit
import ru.myproject.finhelper.ui.deposit.simple.ShowDeposit
import ru.myproject.finhelper.ui.profit.monthPayment.ShowPayment
import ru.myproject.finhelper.ui.profit.roi.ShowROI
import ru.myproject.finhelper.ui.profit.ros.ShowROS
import ru.myproject.finhelper.ui.taxes.income_tax.ShowPersonalTax
import ru.myproject.finhelper.ui.taxes.property_tax.ShowPropertyTax
import ru.myproject.finhelper.ui.taxes.vat.ShowVATAdded


@SuppressLint("SuspiciousIndentation")
@Composable
fun MyAppNavGraph(appViewModelFactory: ViewModelProvider.Factory) {
    var showBottomBar by rememberSaveable() { mutableStateOf(false) }
    val navController = rememberNavController()
    /*Для TopBar
    Позволяет следить за текущим стеком навигации.
    При изменении параметра route Compose может перерисовывать интерфейс
     */
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        var screenShotTriggerCapture: (() -> Unit)? by remember { mutableStateOf(null) }

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
                    composable("ProfitUI") {
                        ProfitChoice(
                            onShowBottomBar = {show -> showBottomBar = show},
                            navController = navController)
                    }
                    composable("DepositUI") {
                        DepositChoice(
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
                                    finViewModelFactory = appViewModelFactory
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
                                    finViewModelFactory = appViewModelFactory
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
                                    finViewModelFactory = appViewModelFactory
                                )
                            }
                        )
                    }
                    composable("ROI_Render") {
                        ScreenShot(navController = navController, currentRoute = currentRoute,
                            onShowBottomBar = {show -> showBottomBar = show},
                            onTriggerCaptureReady = { trigger -> screenShotTriggerCapture = trigger },
                            { modifier ->
                                ShowROI(
                                    onShowBottomBar = {show -> showBottomBar = show},
                                    modifier = modifier,
                                    profitViewModelFactory = appViewModelFactory
                                )
                            }
                        )
                    }

                    composable("ROS_Render") {
                        ScreenShot(navController = navController, currentRoute = currentRoute,
                            onShowBottomBar = {show -> showBottomBar = show},
                            onTriggerCaptureReady = { trigger -> screenShotTriggerCapture = trigger },
                            { modifier ->
                                ShowROS(
                                    onShowBottomBar = {show -> showBottomBar = show},
                                    modifier = modifier,
                                    profitViewModelFactory = appViewModelFactory
                                )
                            }
                        )
                    }

                    composable("Payment_Render") {
                        ScreenShot(navController = navController, currentRoute = currentRoute,
                            onShowBottomBar = {show -> showBottomBar = show},
                            onTriggerCaptureReady = { trigger -> screenShotTriggerCapture = trigger },
                            { modifier ->
                                ShowPayment(
                                    onShowBottomBar = {show -> showBottomBar = show},
                                    modifier = modifier,
                                    profitViewModelFactory = appViewModelFactory
                                )
                            }
                        )
                    }

                    composable("SimpleDep_Render") {
                        ScreenShot(navController = navController, currentRoute = currentRoute,
                            onShowBottomBar = {show -> showBottomBar = show},
                            onTriggerCaptureReady = { trigger -> screenShotTriggerCapture = trigger },
                            { modifier ->
                                ShowDeposit(
                                    onShowBottomBar = {show -> showBottomBar = show},
                                    modifier = modifier,
                                    finViewModelFactory = appViewModelFactory
                                )
                            }
                        )
                    }

                    composable("ComplexDep_Render") {
                        ScreenShot(navController = navController, currentRoute = currentRoute,
                            onShowBottomBar = {show -> showBottomBar = show},
                            onTriggerCaptureReady = { trigger -> screenShotTriggerCapture = trigger },
                            { modifier ->
                                ShowComplexDeposit(
                                    onShowBottomBar = {show -> showBottomBar = show},
                                    modifier = modifier,
                                    finViewModelFactory = appViewModelFactory
                                )
                            }
                        )
                    }

                }
        }
}