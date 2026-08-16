package ru.myproject.finhelper.activity

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.myproject.finhelper.R
import ru.myproject.finhelper.ui.theme.FinHelperTheme
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.testing.TestNavHostController


@Composable
fun WelcomeContent(onShowBottomBar: (Boolean) -> Unit,
                   modifier: Modifier = Modifier,
                   navController: NavController) {

    LaunchedEffect(Unit) {
        onShowBottomBar(true)
    }

    Column(modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = R.drawable.ic_outline_percent_24),
            contentDescription = stringResource(R.string.percentImage),
            modifier = Modifier.size(250.dp)
        )
        Text(
            stringResource(R.string.welcome),
            modifier = Modifier
                .padding(vertical = 80.dp),
            fontSize = 30.sp)
    }
}

@Preview (showBackground = true)
@Composable
fun WelcomeContentPreview() {
    FinHelperTheme {
        val testNavController = TestNavHostController(LocalContext.current)
        WelcomeContent(
            navController = testNavController,
            onShowBottomBar = { }
        )
    }
}