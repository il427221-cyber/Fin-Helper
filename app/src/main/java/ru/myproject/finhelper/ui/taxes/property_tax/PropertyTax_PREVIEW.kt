package ru.myproject.finhelper.ui.taxes.property_tax

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
import ru.myproject.finhelper.ui.taxes.property_tax.ActiveField
import ru.myproject.finhelper.ui.taxes.property_tax.CalculatorPropertyTax
import ru.myproject.finhelper.ui.theme.FinHelperTheme
import ru.myproject.finhelper.viewmodel.FinViewModel
import ru.myproject.finhelper.viewmodel.FinViewModelFactory

@Composable
fun ShowPropertyTax(onShowBottomBar: (Boolean) -> Unit, modifier: Modifier = Modifier,
                    finViewModel: FinViewModel = viewModel(factory = FinViewModelFactory(FinRepositoryImpl()))
) {
    val context = LocalContext.current

    LaunchedEffect(finViewModel) {
        finViewModel.uiMessages.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        onShowBottomBar(false)
    }
    val currentTaxAmount by finViewModel.taxAmount.collectAsState()

    val propertyFocusRequester = remember { FocusRequester() }
    val areaFocusRequester = remember { FocusRequester() }
    val taxFocusRequester = remember { FocusRequester() }
    val shareFocusRequester = remember { FocusRequester() }
    val periodFocusRequester = remember { FocusRequester() }

    var propertyInput by remember { mutableStateOf("") }
    var areaInput by remember { mutableStateOf("") }
    var taxInput by remember { mutableStateOf("") }
    var shareInput by remember { mutableStateOf("") }
    var periodInput by remember { mutableStateOf("") }

    var activeField by remember { mutableStateOf(ActiveField.NONE) }

    LaunchedEffect(Unit) {
        propertyFocusRequester.requestFocus()
        activeField = ActiveField.PROPERTY
    }

    // 1.Обработка нажатий на цифры
    val handleNumberInput: (String) -> Unit = remember {
        { digit ->
            when (activeField) {
                ActiveField.PROPERTY -> { propertyInput += digit }
                ActiveField.AREA -> { areaInput += digit }
                ActiveField.TAX -> { taxInput += digit }
                ActiveField.SHARE -> { shareInput += digit }
                ActiveField.PERIOD -> { periodInput += digit}
                ActiveField.NONE -> { /*ничего не делаем*/ }
            }
        }
    }
    // 2.Обработка нажатий на запятую
    val handleCommaClick: () -> Unit = remember {
        {
            when (activeField) {
                ActiveField.PROPERTY -> {
                    if (!propertyInput.contains('.') && !propertyInput.contains(',')) propertyInput += "."
                }

                ActiveField.AREA -> {
                    if (!areaInput.contains('.') && !areaInput.contains(',')) areaInput += "."
                }

                ActiveField.TAX -> {
                    if (!taxInput.contains('.') && !taxInput.contains(',')) taxInput += "."
                }

                ActiveField.SHARE -> {
                    if (!shareInput.contains('.') && !shareInput.contains(',')) shareInput += "."
                }
                ActiveField.PERIOD -> {
                    if (!periodInput.contains('.') && !periodInput.contains(',')) periodInput += "."
                }

                ActiveField.NONE -> { /*ничего не делаем*/ }
            }
        }
    }
    // 3.Обработка нажатий на кнопку с крестиком
    val handleDeleteClick: () -> Unit = remember {
        {
            when (activeField) {
                ActiveField.PROPERTY -> if (propertyInput.isNotEmpty()) propertyInput = propertyInput.dropLast(1)
                ActiveField.AREA -> if (areaInput.isNotEmpty()) areaInput = areaInput.dropLast(1)
                ActiveField.TAX -> if (taxInput.isNotEmpty()) taxInput = taxInput.dropLast(1)
                ActiveField.SHARE -> if (shareInput.isNotEmpty()) shareInput = shareInput.dropLast(1)
                ActiveField.PERIOD -> if (periodInput.isNotEmpty()) periodInput = periodInput.dropLast(1)
                ActiveField.NONE -> { /*ничего не делаем*/ }
            }
        }
    }
    // 4.Обработка нажатий на корзину
    val handleClearClick: () -> Unit = remember {
        {
            propertyInput = ""
            areaInput = ""
            taxInput = ""
            shareInput = ""
            periodInput = ""
            finViewModel.clearOutputFields()
            propertyFocusRequester.requestFocus()
            activeField = ActiveField.PROPERTY
        }
    }
    // 5. Обработка нажатий на стрелку вниз
    val handleMoveCursorDownClick: () -> Unit = remember {
        {
            when (activeField) {
                ActiveField.PROPERTY -> {
                    areaFocusRequester.requestFocus()
                    activeField = ActiveField.AREA
                }

                ActiveField.AREA -> {
                    taxFocusRequester.requestFocus()
                    activeField = ActiveField.TAX
                }

                ActiveField.TAX -> {
                    shareFocusRequester.requestFocus()
                    activeField = ActiveField.SHARE
                }

                ActiveField.SHARE -> {
                    periodFocusRequester.requestFocus()
                    activeField = ActiveField.PERIOD
                }

                else -> {
                    propertyFocusRequester.requestFocus()
                    activeField = ActiveField.PROPERTY
                }

            }
        }
    }
    // 6. Обработка нажатий на стрелку вверх
    val handleMoveCursorUpClick: () -> Unit = remember {
        {
            when (activeField) {
                ActiveField.PROPERTY -> {
                    periodFocusRequester.requestFocus()
                    activeField = ActiveField.PERIOD
                }

                ActiveField.AREA -> {
                    propertyFocusRequester.requestFocus()
                    activeField = ActiveField.PROPERTY
                }

                ActiveField.TAX -> {
                    areaFocusRequester.requestFocus()
                    activeField = ActiveField.AREA
                }

                ActiveField.SHARE -> {
                    taxFocusRequester.requestFocus()
                    activeField = ActiveField.TAX
                }

                ActiveField.PERIOD -> {
                    shareFocusRequester.requestFocus()
                    activeField = ActiveField.SHARE
                }

                else -> {
                    propertyFocusRequester.requestFocus()
                    activeField = ActiveField.PROPERTY
                }

            }
        }
    }

    // 7.Обработка нажатий на кнопку расчета Налога
    val onCalculateButtonClick: (Double, Double, Double,Double,Double) -> Unit = remember {
        { property, area, tax, share, period ->
            finViewModel.calculate_PropertyTax(property, area,tax, share, period) }
    }

    val onPropertyInputChanged: (String) -> Unit = { newValue ->
        propertyInput = newValue
        activeField = ActiveField.PROPERTY
    }

    val onAreaInputChanged: (String) -> Unit = { newValue ->
        areaInput = newValue
        activeField = ActiveField.AREA
    }

    val onTaxInputChanged: (String) -> Unit = { newValue ->
        taxInput = newValue
        activeField = ActiveField.TAX
    }

    val onShareInputChanged: (String) -> Unit = { newValue ->
        shareInput = newValue
        activeField = ActiveField.SHARE
    }

    val onPeriodInputChanged: (String) -> Unit = { newValue ->
        periodInput = newValue
        activeField = ActiveField.PERIOD
    }

    val onActiveFieldChanged: (ActiveField) -> Unit = remember {
        { newActiveField ->
            activeField = newActiveField

            when (newActiveField) {
                ActiveField.PROPERTY -> propertyFocusRequester.requestFocus()
                ActiveField.AREA -> areaFocusRequester.requestFocus()
                ActiveField.TAX -> taxFocusRequester.requestFocus()
                ActiveField.SHARE -> shareFocusRequester.requestFocus()
                ActiveField.PERIOD -> periodFocusRequester.requestFocus()
                ActiveField.NONE -> { /* При сбросе фокуса на NONE, не фокусируемся ни на чем */ }
            }
        }
    }

    CalculatorPropertyTax(
        currentTaxAmount = currentTaxAmount,
        propertyInputValue = propertyInput,
        areaInputValue = areaInput,
        taxInputValue = taxInput,
        shareInputValue = shareInput,
        periodInputValue = periodInput,
        activeField = activeField,
        onPropertyInputChanged = onPropertyInputChanged,
        onAreaInputChanged = onAreaInputChanged,
        onTaxInputChanged = onTaxInputChanged,
        onShareInputChanged = onShareInputChanged,
        onPeriodInputChanged = onPeriodInputChanged,
        onActiveFieldChanged = onActiveFieldChanged,
        onCalculateTaxClick = onCalculateButtonClick,
        onNumberClick = handleNumberInput,
        onCommaClick = handleCommaClick,
        onDeleteClick = handleDeleteClick,
        onClearClick = handleClearClick,
        onMoveCursorDownClick = handleMoveCursorDownClick,
        onMoveCursorUpClick = handleMoveCursorUpClick,
        modifier = modifier,
        propertyFocusRequester = propertyFocusRequester,
        areaFocusRequester = areaFocusRequester,
        taxFocusRequester = taxFocusRequester,
        shareFocusRequester = shareFocusRequester,
        periodFocusRequester = periodFocusRequester
    )
}

@Preview (showBackground = true)
@Composable
fun ShowPropertyTaxPreview() {
    FinHelperTheme {
        ShowPropertyTax(onShowBottomBar = { })
    }
}