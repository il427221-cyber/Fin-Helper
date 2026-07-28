package ru.myproject.finhelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.myproject.finhelper.navGraph.MyAppNavGraph
import ru.myproject.finhelper.ui.theme.FinHelperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinHelperTheme {
                MyAppNavGraph()
            }
        }
    }
}

