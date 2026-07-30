package ru.myproject.finhelper.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import ru.myproject.finhelper.ui.theme.FinHelperTheme
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.testing.TestNavHostController

@Composable
fun VATOperations(modifier: Modifier = Modifier,navController: NavController) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(onClick = {navController.navigate("VATCalc_UI")}) {
            Text("Add VAT")
        }
        TextButton(onClick = {}) {
            Text("Extract VAT")
        }
    }
}

@Preview
@Composable
fun VATOperationsPreview() {
    FinHelperTheme {
        val testNavController = TestNavHostController(LocalContext.current)
        TaxesChoice(navController = testNavController)
    }
}