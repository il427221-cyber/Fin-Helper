package ru.myproject.finhelper.ui.taxes.vat

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
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.myproject.finhelper.repository.FinRepositoryImpl
import ru.myproject.finhelper.ui.keyboard.CustomKeyBoard
import ru.myproject.finhelper.ui.theme.FinHelperTheme
import ru.myproject.finhelper.viewmodel.FinViewModel
import ru.myproject.finhelper.viewmodel.FinViewModelFactory

@Composable
fun ShowVATAdded(onShowBottomBar: (Boolean) -> Unit, modifier: Modifier = Modifier,
    finViewModel: FinViewModel = viewModel(factory = FinViewModelFactory(FinRepositoryImpl()))
) {
    val context = LocalContext.current

    LaunchedEffect(finViewModel) {
        finViewModel.uiMessages.collect{message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        onShowBottomBar(false)
    }
    val currentVATAmount by finViewModel.taxAmount.collectAsState()
    val currentTotalAmount by finViewModel.totalWithTax.collectAsState()

    val sumFocusRequester = remember { FocusRequester() }
    val taxFocusRequester = remember { FocusRequester() }

    var sumInput by remember { mutableStateOf("") }
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
                ActiveField.TAX -> {
                    taxInput += digit
                }
                ActiveField.NONE -> { /*ничего не делаем*/ }
            }
        }
    }
    // 2.Обработка нажатий на запятую
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
    // 3.Обработка нажатий на кнопку с крестиком
    val handleDeleteClick: () -> Unit = remember {
        {
            when(activeField) {
                ActiveField.SUM -> if(sumInput.isNotEmpty()) sumInput = sumInput.dropLast(1)
                ActiveField.TAX -> if(taxInput.isNotEmpty()) taxInput = taxInput.dropLast(1)
                ActiveField.NONE -> { /*ничего не делаем*/  }
            }
        }
    }
    // 4.Обработка нажатий на корзину
    val handleClearClick: () -> Unit = remember {
        {
            sumInput = ""
            taxInput = ""
            finViewModel.clearOutputFields()
            sumFocusRequester.requestFocus()
            activeField = ActiveField.SUM
        }
    }
    // 5. Обработка нажатий на стрелки (вверх и вниз)
    val handleMoveCursorClick: () -> Unit = remember {
        {
            when(activeField) {
                ActiveField.SUM -> {
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

    // 6.Обработка нажатий на кнопку начисления НДС
    val onCalculateVATButtonClick: (Double,Double) -> Unit = remember {
        { sum,tax -> finViewModel.calculate_VAT_And_Total(sum,tax) }
    }
    // 7.Обработка нажатий на кнопку выделения НДС
    val onExtractVATButtonClick: (Double,Double) -> Unit = remember {
        {sum,tax -> finViewModel.extract_VAT_And_Total(sum,tax) }
    }

    val onSumInputChanged: (String) -> Unit = { newValue ->
        sumInput = newValue
        activeField = ActiveField.SUM
    }

    val onTaxInputChanged: (String) -> Unit = { newValue ->
        taxInput = newValue
        activeField = ActiveField.TAX
    }

    val onActiveFieldChanged: (ActiveField) -> Unit = remember {
        {newActiveField ->
            activeField = newActiveField

            when (newActiveField) {
                ActiveField.SUM -> sumFocusRequester.requestFocus()
                ActiveField.TAX -> taxFocusRequester.requestFocus()
                ActiveField.NONE -> { /* При сбросе фокуса на NONE, не фокусируемся ни на чем */ }
            }
        }
    }

    CalculatorDisplay(
        currentVatAmount = currentVATAmount,
        currentTotalAmount = currentTotalAmount,
        sumInputValue = sumInput,
        taxInputValue = taxInput,
        activeField = activeField,
        onSumInputChanged = onSumInputChanged,
        onTaxInputChanged = onTaxInputChanged,
        onActiveFieldChanged = onActiveFieldChanged,
        onCalculateVATClick = onCalculateVATButtonClick,
        onExtractVATClick = onExtractVATButtonClick,
        onNumberClick = handleNumberInput,
        onCommaClick = handleCommaClick,
        onDeleteClick = handleDeleteClick,
        onClearClick = handleClearClick,
        onMoveCursorClick = handleMoveCursorClick,
        modifier = modifier,
        sumFocusRequester = sumFocusRequester,
        taxFocusRequester = taxFocusRequester
    )
}

@Preview (showBackground = true)
@Composable
fun ShowVATAddedPreview() {
    FinHelperTheme {
        ShowVATAdded(onShowBottomBar = { })
    }
}