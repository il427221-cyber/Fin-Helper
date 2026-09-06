package ru.myproject.finhelper.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import ru.myproject.finhelper.ui.theme.FinHelperTheme
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.testing.TestNavHostController

@Composable
fun ProfitChoice(onShowBottomBar: (Boolean) -> Unit,
                modifier: Modifier = Modifier,
                navController: NavController) {

    LaunchedEffect(Unit) {
        onShowBottomBar(true)
    }

    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(onClick = {navController.navigate("ROI_Render")}) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Индекс ROI")
                Text("(Окупаемость инвестиций)")
            }

        }
        TextButton(onClick = {}) {
            Text("")
        }
        TextButton(onClick = {}) {
            Text("")
        }
    }

}
@Preview
@Composable
fun ProfitChoicePreview() {
    FinHelperTheme {
        val testNavController = TestNavHostController(LocalContext.current)
        ProfitChoice(navController = testNavController, onShowBottomBar = { })
    }
}