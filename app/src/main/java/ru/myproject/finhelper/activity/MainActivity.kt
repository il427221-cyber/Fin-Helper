package ru.myproject.finhelper.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import ru.myproject.finhelper.navigation.MyAppNavGraph
import ru.myproject.finhelper.repository.FinRepository
import ru.myproject.finhelper.repository.FinRepositoryImpl
import ru.myproject.finhelper.repository.ProfitRepository
import ru.myproject.finhelper.repository.ProfitRepositoryImpl
import ru.myproject.finhelper.ui.theme.FinHelperTheme
import ru.myproject.finhelper.viewmodel.FinViewModelFactory

class MainActivity : ComponentActivity() {

    // Инициализируем репозитории на уровне Activity
    private val finRepository: FinRepository = FinRepositoryImpl()
    private val profitRepository: ProfitRepository = ProfitRepositoryImpl()

    // Инициализируем фабрику на уровне Activity
    private val appViewModelFactory: ViewModelProvider.Factory by lazy {
        FinViewModelFactory(finRepository, profitRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinHelperTheme {
                MyAppNavGraph(appViewModelFactory = appViewModelFactory)
            }
        }
    }
}