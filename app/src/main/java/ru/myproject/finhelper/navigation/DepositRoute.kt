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
fun DepositChoice(onShowBottomBar: (Boolean) -> Unit,
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
        TextButton(onClick = {navController.navigate("SimpleDep_Render")}) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.deposit_with_interest))
                Text(stringResource(R.string.without_capitalization_with_yearly_capitalization))
            }
        }

        TextButton(onClick = {navController.navigate("ComplexDep_Render")}) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.deposit_with_interest))
                Text(stringResource(R.string.with_periodical_capitalization))
            }
        }
    }

}
@Preview
@Composable
fun DepositChoicePreview() {
    FinHelperTheme {
        val testNavController = TestNavHostController(LocalContext.current)
        TaxesChoice(navController = testNavController, onShowBottomBar = { })
    }
}