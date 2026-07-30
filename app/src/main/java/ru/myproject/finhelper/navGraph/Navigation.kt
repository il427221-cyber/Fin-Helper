package ru.myproject.finhelper.navGraph

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import ru.myproject.finhelper.BottomAppBarHome
import ru.myproject.finhelper.ui.WelcomeContent
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.myproject.finhelper.TopAppBarBack
import ru.myproject.finhelper.ui.CalculatorDisplay
import ru.myproject.finhelper.ui.ShowVATAdded
import ru.myproject.finhelper.ui.TaxesChoice
import ru.myproject.finhelper.ui.VATOperations


@SuppressLint("SuspiciousIndentation")
@Composable
fun MyAppNavGraph() {
    val navController = rememberNavController()

    /*Для TopBar
    Позволяет следить за текущим стеком навигации.
    При изменении параметра route Compose может перерисовывать интерфейс
     */
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Scaffold(
            topBar = {TopAppBarBack(navController = navController, currentRoute = currentRoute)},
            bottomBar = { (BottomAppBarHome(navController = navController)) }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "HomeScreenUI",
                modifier = Modifier.padding(innerPadding))

                {
                    composable("HomeScreenUI") {
                        WelcomeContent(navController = navController)
                    }
                    composable("TaxesUI") {
                        TaxesChoice(navController = navController)
                    }
                    composable("VAT_UI") {
                        VATOperations(navController = navController)
                    }
                    composable("VATCalc_UI") {
                        //TODO
                    }


                }

        }
}