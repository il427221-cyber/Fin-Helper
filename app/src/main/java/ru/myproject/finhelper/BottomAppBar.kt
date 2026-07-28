package ru.myproject.finhelper

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import ru.myproject.finhelper.ui.theme.FinHelperTheme



@Composable
fun BottomAppBarHome(modifier: Modifier = Modifier,navController: NavController) {
    BottomAppBar(modifier = Modifier.padding(8.dp), actions = {
        TextButton(onClick = {navController.navigate("TaxesUI")}) {
            Text(stringResource(R.string.taxes))
        }
        TextButton(onClick = {}) {
            Text("Прибыль")
        }
        TextButton(onClick = {}) {
            Text("Конвертер\nВалют", maxLines = 2)
        }
        TextButton(onClick = {}) {
            Text("Больничные/\nОтпускные", maxLines = 2)
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