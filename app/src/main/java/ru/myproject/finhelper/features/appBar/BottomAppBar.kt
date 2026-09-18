package ru.myproject.finhelper.features.appBar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import ru.myproject.finhelper.R
import ru.myproject.finhelper.ui.theme.FinHelperTheme

@Composable
fun BottomAppBarHome(modifier: Modifier = Modifier,navController: NavController) {
        BottomAppBar(modifier = Modifier.padding(8.dp),
            actions = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = { navController.navigate("TaxesUI") }) {
                        Text(stringResource(R.string.taxes))
                    }
                    TextButton(onClick = { navController.navigate("ProfitUI")}) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(stringResource(R.string.Profitability))
                            Text(stringResource(R.string.of_organizations))
                        }
                    }
                    TextButton(onClick = {navController.navigate("DepositUI")}) {
                        Text("Вклады")
                    }
                }
            }
        )
}

@Preview
@Composable
fun BottomAppBarPreview() {
    FinHelperTheme {
        val testNavController = TestNavHostController(LocalContext.current)
        BottomAppBarHome(navController = testNavController)
    }
}