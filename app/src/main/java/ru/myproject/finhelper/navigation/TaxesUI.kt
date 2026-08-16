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
import androidx.compose.ui.res.stringResource
import androidx.navigation.testing.TestNavHostController
import ru.myproject.finhelper.R

@Composable
fun TaxesChoice(onShowBottomBar: (Boolean) -> Unit,
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
        TextButton(onClick = {navController.navigate("VATCalc_UI")}) {
            Text(stringResource(R.string.vat))
        }
        TextButton(onClick = {}) {
            Text(stringResource(R.string.personal_income_tax))
        }
        TextButton(onClick = {}) {
            Text(stringResource(R.string.income_tax))
        }
    }

}
@Preview
@Composable
fun TaxesChoicePreview() {
    FinHelperTheme {
        val testNavController = TestNavHostController(LocalContext.current)
        TaxesChoice(navController = testNavController, onShowBottomBar = { })
    }
}
