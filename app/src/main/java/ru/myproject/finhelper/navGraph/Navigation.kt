package ru.myproject.finhelper.navGraph

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import ru.myproject.finhelper.BottomAppBarHome
import ru.myproject.finhelper.ui.WelcomeContent
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.myproject.finhelper.ScreenShot
import ru.myproject.finhelper.TopAppBarBack
import ru.myproject.finhelper.ui.TaxesChoice


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

        Scaffold(
            topBar = {TopAppBarBack(navController = navController, currentRoute = currentRoute, onCaptureClick = {} )},
            bottomBar = { if(showBottomBar) BottomAppBarHome(navController = navController) }
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
                            onShowBottomBar = {show -> showBottomBar = show})
                    }
                }
        }
}