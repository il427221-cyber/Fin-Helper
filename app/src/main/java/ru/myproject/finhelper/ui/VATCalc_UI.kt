package ru.myproject.finhelper.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.myproject.finhelper.NumericInputField
import ru.myproject.finhelper.R
import ru.myproject.finhelper.repository.FinRepositoryImpl
import ru.myproject.finhelper.ui.theme.FinHelperTheme
import ru.myproject.finhelper.viewmodel.FinViewModel
import ru.myproject.finhelper.viewmodel.FinViewModelFactory

@Composable
fun ShowVATAdded(
    modifier: Modifier = Modifier,
    finViewModel: FinViewModel = viewModel(factory = FinViewModelFactory(FinRepositoryImpl()))
) {
    val currentVATAmount by finViewModel.vatAmount.collectAsState()
    val currentTotalAmount by finViewModel.totalWithVat.collectAsState()

}

@Composable
fun CalculatorDisplay(vatAmount: Double,
                      totalWithVat: Double,
                      onCalculateClick: (sum: Double, tax: Double) -> Unit,
                      modifier: Modifier = Modifier)
{
    var sumInput by remember { mutableStateOf("") }
    var taxInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {// включает 2 Box

        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NumericInputField(
                    text = "Сумма:",
                    number = sumInput,
                    onValueChange = { sumInput = it },
                    textHint = "руб."
                )

                Spacer(modifier = Modifier.height(32.dp))

                NumericInputField(
                    text = "Ставка:",
                    number = sumInput,
                    onValueChange = { taxInput = it },
                    textHint = "%"
                    )

                Spacer(modifier = Modifier.height(32.dp))

                NumericInputField(
                    text = "НДС:",
                    number = sumInput,
                    onValueChange = { sumInput = it },
                    textHint = "руб."
                )

                Spacer(modifier = Modifier.height(32.dp))

                NumericInputField(
                    text = "Сумма с\n НДС:",
                    number = sumInput,
                    onValueChange = { sumInput = it },
                    textHint = "руб."
                )
            }
        }

        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.BottomStart) {
            Text("Нижняя часть", style = TextStyle(fontSize = 16.sp))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CalculatorDisplayPreview() {
    FinHelperTheme {
        CalculatorDisplay(vatAmount = 20.0, totalWithVat = 120.0,
            onCalculateClick = { sum, tax ->
                println("Кнопка 'Рассчитать НДС' нажата в Preview с суммой $sum и налогом $tax") })
    }
}