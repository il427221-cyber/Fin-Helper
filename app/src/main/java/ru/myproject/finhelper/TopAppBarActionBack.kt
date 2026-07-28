package ru.myproject.finhelper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarBack(modifier: Modifier = Modifier,navController: NavController, currentRoute: String?) {
    TopAppBar(
        title = {
            if(currentRoute != "HomeScreenUI") {
                Text(stringResource(R.string.return_back))
            }
                },
        navigationIcon = {
            if(currentRoute != "HomeScreenUI") {
            IconButton(onClick = {navController.navigate("HomeScreenUI")}) {
                Icon(Icons.Filled.ArrowBackIosNew,
                    contentDescription = stringResource(R.string.return_back))
            }
            }
        }
    )
}