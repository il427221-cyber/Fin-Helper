package ru.myproject.finhelper.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.myproject.finhelper.CustomKeyBoard
import ru.myproject.finhelper.NumericInputField
import ru.myproject.finhelper.R
import ru.myproject.finhelper.repository.FinRepositoryImpl
import ru.myproject.finhelper.ui.theme.FinHelperTheme
import ru.myproject.finhelper.viewmodel.FinViewModel
import ru.myproject.finhelper.viewmodel.FinViewModelFactory


enum class ActiveField {
    SUM, TAX, NONE
}
@Composable
fun ShowVATAdded(
    modifier: Modifier = Modifier,
    finViewModel: FinViewModel = viewModel(factory = FinViewModelFactory(FinRepositoryImpl()))
) {
    val currentVATAmount by finViewModel.vatAmount.collectAsState()
    val currentTotalAmount by finViewModel.totalWithVat.collectAsState()


}

@Composable
fun CalculatorDisplay(
    vatAmount: Double,
    totalWithVat: Double,
    onCalculateClick: (sum: Double, tax: Double) -> Unit,
    modifier: Modifier = Modifier)
{
    var sumInput by remember { mutableStateOf("") }
    var taxInput by remember { mutableStateOf("") }
    var activeField by remember { mutableStateOf(ActiveField.NONE) }

    // Обработка нажатий на цифры
    val handleNumberInput: (String) -> Unit = remember {
        { digit ->
            when (activeField) {
                ActiveField.SUM -> sumInput += digit
                ActiveField.TAX -> taxInput += digit
                ActiveField.NONE -> {/*ничего не делаем*/}
            }
        }
    }
    //Обработка нажатий на запятую
    val handleCommaClick: () -> Unit = remember {
        {
            when(activeField) {
                ActiveField.SUM -> {
                    if(!sumInput.contains('.') && !sumInput.contains(',')) sumInput+= "."
                }
                ActiveField.TAX -> {
                    if(!taxInput.contains('.') && !taxInput.contains(',')) taxInput+= "."
                }
                ActiveField.NONE -> { /*ничего не делаем*/  }
            }
        }
    }
    //Обработка нажатий на кнопку с крестиком
    val handleDeleteClick: () -> Unit = remember {
        {
            when(activeField) {
                ActiveField.SUM -> if(sumInput.isNotEmpty()) sumInput = sumInput.dropLast(1)
                ActiveField.TAX -> if(taxInput.isNotEmpty()) taxInput = taxInput.dropLast(1)
                ActiveField.NONE -> { /*ничего не делаем*/  }
            }
        }
    }
    // Обработка нажатий на корзину
    val handleClearClick: () -> Unit = remember {
        {
            sumInput = ""
            taxInput = ""
            activeField = ActiveField.NONE

        }
    }
    val handleMoveCursorDown: () -> Unit = {
        //TODO
    }

    val handleMoveCursorUp: () -> Unit = {
       //TODO
    }


    Column(modifier = Modifier.fillMaxSize()) {// включает 2 Box

        Box(modifier = Modifier
            .weight(1f)
            .fillMaxWidth(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NumericInputField(
                    text = "Сумма:",
                    number = sumInput,
                    onValueChange = { sumInput = it },
                    textHint = "руб.",
                    onFocusGained = {activeField = ActiveField.SUM}
                )

                Spacer(modifier = Modifier.height(32.dp))

                NumericInputField(
                    text = "Ставка:",
                    number = sumInput,
                    onValueChange = { taxInput = it },
                    textHint = "%",
                    onFocusGained = {activeField = ActiveField.TAX}
                    )

                Spacer(modifier = Modifier.height(32.dp))

                NumericInputField(
                    text = "НДС:",
                    number = sumInput,
                    onValueChange = { sumInput = it },
                    textHint = "руб.",
                    onFocusGained = {activeField = ActiveField.SUM}
                )

                Spacer(modifier = Modifier.height(32.dp))

                NumericInputField(
                    text = "Итоговая\n сумма:",
                    number = sumInput,
                    onValueChange = { sumInput = it },
                    textHint = "руб.",
                    onFocusGained = {activeField = ActiveField.SUM}
                )
                //TODO Buttons (ADD VAT, EXTRACT VAT)
            }
        }

        Box(modifier = Modifier
            .weight(1f)
            .fillMaxWidth(), contentAlignment = Alignment.Center) {
            CustomKeyBoard(
                onNumberClick = handleNumberInput,
                onCommaClick = handleCommaClick,
                onDeleteClick = handleDeleteClick,
                onClearClick = handleClearClick,
                onMoveCursorDownClick = {},
                onMoveCursorUpClick = {}
            )
        }
    }
}

//@Composable
//@Preview(showBackground = true)
//fun CalculatorDisplayPreview() {
//    FinHelperTheme {
//        CalculatorDisplay(
////            vatAmount = 20.0,
////            totalWithVat = 120.0,
//            onCalculateClick = { sum, tax ->
//                println("Кнопка 'Рассчитать НДС' нажата в Preview с суммой $sum и налогом $tax") })
//    }
//}