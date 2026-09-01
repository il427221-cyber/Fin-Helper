package ru.myproject.finhelper.ui.taxes.income_tax

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.myproject.finhelper.repository.FinRepositoryImpl
import ru.myproject.finhelper.repository.ProfitRepositoryImpl
import ru.myproject.finhelper.ui.theme.FinHelperTheme
import ru.myproject.finhelper.viewmodel.FinViewModel
import ru.myproject.finhelper.viewmodel.FinViewModelFactory
import ru.myproject.finhelper.viewmodel.ProfitViewModel

@Composable
fun ShowPersonalTax(onShowBottomBar: (Boolean) -> Unit, modifier: Modifier = Modifier,
                    finViewModelFactory: ViewModelProvider.Factory)
{
    val context = LocalContext.current

    val finViewModel: FinViewModel = viewModel(factory = finViewModelFactory)

    LaunchedEffect(finViewModel) {
        finViewModel.uiMessages.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        onShowBottomBar(false)
    }
    val currentTaxAmount by finViewModel.taxAmount.collectAsState()
    val currentTotalAmount by finViewModel.totalWithTax.collectAsState()

    val sumFocusRequester = remember { FocusRequester() }
    val deductionFocusRequester = remember { FocusRequester() }
    val taxFocusRequester = remember { FocusRequester() }

    var sumInput by remember { mutableStateOf("") }
    var deductionInput by remember { mutableStateOf("") }
    var taxInput by remember { mutableStateOf("") }

    var activeField by remember { mutableStateOf(ActiveField.NONE) }

    LaunchedEffect(Unit) {
        sumFocusRequester.requestFocus() // Запрашиваем фокус для "Суммы" при запуске
        activeField = ActiveField.SUM // Обновляем активное поле
    }

    // 1.Обработка нажатий на цифры
    val handleNumberInput: (String) -> Unit = remember {
        { digit ->
            when (activeField) {
                ActiveField.SUM -> {
                    sumInput += digit
                }

                ActiveField.DEDUCTION -> {
                    deductionInput += digit
                }

                ActiveField.TAX -> {
                    taxInput += digit
                }

                ActiveField.NONE -> { /*ничего не делаем*/
                }

            }
        }
    }
    // 2.Обработка нажатий на запятую
    val handleCommaClick: () -> Unit = remember {
        {
            when (activeField) {
                ActiveField.SUM -> {
                    if (!sumInput.contains('.') && !sumInput.contains(',')) sumInput += "."
                }

                ActiveField.DEDUCTION -> {
                    if (!deductionInput.contains('.') && !sumInput.contains(',')) deductionInput += "."
                }

                ActiveField.TAX -> {
                    if (!taxInput.contains('.') && !taxInput.contains(',')) taxInput += "."
                }

                ActiveField.NONE -> { /*ничего не делаем*/
                }


            }
        }
    }
    // 3.Обработка нажатий на кнопку с крестиком
    val handleDeleteClick: () -> Unit = remember {
        {
            when (activeField) {
                ActiveField.SUM -> if (sumInput.isNotEmpty()) sumInput = sumInput.dropLast(1)
                ActiveField.DEDUCTION -> if (deductionInput.isNotEmpty()) deductionInput = deductionInput.dropLast(1)
                ActiveField.TAX -> if (taxInput.isNotEmpty()) taxInput = taxInput.dropLast(1)
                ActiveField.NONE -> { /*ничего не делаем*/
                }
            }
        }
    }
    // 4.Обработка нажатий на корзину
    val handleClearClick: () -> Unit = remember {
        {
            sumInput = ""
            deductionInput = ""
            taxInput = ""
            finViewModel.clearOutputFields()
            sumFocusRequester.requestFocus()
            activeField = ActiveField.SUM
        }
    }
    // 5. Обработка нажатий на стрелку вниз
    val handleMoveCursorDownClick: () -> Unit = remember {
        {
            when (activeField) {
                ActiveField.SUM -> {
                    deductionFocusRequester.requestFocus()
                    activeField = ActiveField.DEDUCTION
                }

                ActiveField.DEDUCTION -> {
                    taxFocusRequester.requestFocus()
                    activeField = ActiveField.TAX
                }

                ActiveField.TAX -> {
                    sumFocusRequester.requestFocus()
                    activeField = ActiveField.SUM
                }

                ActiveField.NONE -> {
                    sumFocusRequester.requestFocus()
                    activeField = ActiveField.SUM
                }
            }
        }
    }
    // 6. Обработка нажатий на стрелку вверх
    val handleMoveCursorUpClick: () -> Unit = remember {
        {
            when (activeField) {
                ActiveField.SUM -> {
                    taxFocusRequester.requestFocus()
                    activeField = ActiveField.TAX
                }

                ActiveField.DEDUCTION -> {
                    sumFocusRequester.requestFocus()
                    activeField = ActiveField.SUM
                }

                ActiveField.TAX -> {
                    deductionFocusRequester.requestFocus()
                    activeField = ActiveField.DEDUCTION
                }

                ActiveField.NONE -> {
                    sumFocusRequester.requestFocus()
                    activeField = ActiveField.SUM
                }
            }
        }
    }

    // 7.Обработка нажатий на кнопку расчета НДФЛ
    val onCalculateButtonClick: (Double, Double, Double) -> Unit = remember {
        { sum, deduction, tax -> finViewModel.calculate_PersonalTax(sum, deduction,tax) }
    }

    val onSumInputChanged: (String) -> Unit = { newValue ->
        sumInput = newValue
        activeField = ActiveField.SUM
    }

    val onDeductionInputChanged: (String) -> Unit = { newValue ->
        deductionInput = newValue
        activeField = ActiveField.DEDUCTION
    }

    val onTaxInputChanged: (String) -> Unit = { newValue ->
        taxInput = newValue
        activeField = ActiveField.TAX
    }

    val onActiveFieldChanged: (ActiveField) -> Unit = remember {
        { newActiveField ->
            activeField = newActiveField

            when (newActiveField) {
                ActiveField.SUM -> sumFocusRequester.requestFocus()
                ActiveField.DEDUCTION -> deductionFocusRequester.requestFocus()
                ActiveField.TAX -> taxFocusRequester.requestFocus()
                ActiveField.NONE -> { /* При сбросе фокуса на NONE, не фокусируемся ни на чем */
                }
            }
        }
    }

    CalculatorPersonalTax(
        currentTaxAmount = currentTaxAmount,
        currentTotalAmount = currentTotalAmount,
        sumInputValue = sumInput,
        deductionInputValue = deductionInput,
        taxInputValue = taxInput,
        activeField = activeField,
        onSumInputChanged = onSumInputChanged,
        onDeductionInputChanged = onDeductionInputChanged ,
        onTaxInputChanged = onTaxInputChanged,
        onActiveFieldChanged = onActiveFieldChanged,
        onCalculateTaxClick = onCalculateButtonClick,
        onNumberClick = handleNumberInput,
        onCommaClick = handleCommaClick,
        onDeleteClick = handleDeleteClick,
        onClearClick = handleClearClick,
        onMoveCursorDownClick = handleMoveCursorDownClick,
        onMoveCursorUpClick = handleMoveCursorUpClick,
        modifier = modifier,
        sumFocusRequester = sumFocusRequester,
        deductionFocusRequester = deductionFocusRequester,
        taxFocusRequester = taxFocusRequester
    )
}

//@Preview (showBackground = true)
//@Composable
//fun ShowPersonalTaxPreview() {
//    FinHelperTheme {
//        ShowPersonalTax(onShowBottomBar = { })
//    }
//}