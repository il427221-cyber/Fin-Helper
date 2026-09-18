package ru.myproject.finhelper.ui.deposit.simple

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.myproject.finhelper.viewmodel.TaxViewModel
import ru.myproject.finhelper.dto.tax_ui_state.ActiveField
import ru.myproject.finhelper.dto.tax_ui_state.TaxUIState
import ru.myproject.finhelper.ui.MockApplicationForPreview
import ru.myproject.finhelper.ui.PreviewTaxViewModelFactory
import ru.myproject.finhelper.ui.theme.FinHelperTheme

@Composable
fun ShowDeposit(onShowBottomBar: (Boolean) -> Unit, modifier: Modifier = Modifier,
                 finViewModelFactory: ViewModelProvider.Factory)
{
    val context = LocalContext.current
    val taxViewModel: TaxViewModel = viewModel(factory = finViewModelFactory)

    LaunchedEffect(taxViewModel) {
        taxViewModel.uiMessages.collect{ message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        onShowBottomBar(false)
    }

    val taxUiState by taxViewModel.taxUiState.collectAsState()
    val sumFocusRequester = remember { FocusRequester() }
    val rateFocusRequester = remember { FocusRequester() }
    val periodFocusRequester = remember { FocusRequester() }

    LaunchedEffect(taxViewModel) {
        when(taxUiState.activeField) {
            ActiveField.SUM -> sumFocusRequester.requestFocus()
            ActiveField.RATE -> rateFocusRequester.requestFocus()
            ActiveField.PERIOD -> periodFocusRequester.requestFocus()
            else -> {
                sumFocusRequester.freeFocus()
                rateFocusRequester.freeFocus()
            }
        }
    }

    val handleNumberInput: (String) -> Unit =  {number ->
        taxViewModel.appendNumberToActiveField(number)
    }

    val handleCommaClick: () -> Unit =  { taxViewModel.appendCommaToActiveField() }

    val handleDeleteClick: () -> Unit =  { taxViewModel.deleteLastCharFromActiveField() }

    val handleClearClick: () -> Unit = {
        taxViewModel.clearAllFields()
        sumFocusRequester.requestFocus()
    }

    val handleMoveCursorDown: () -> Unit = remember {
        {
            when(taxUiState.activeField) {
                ActiveField.SUM -> { rateFocusRequester.requestFocus() }
                ActiveField.RATE -> { periodFocusRequester.requestFocus() }
                ActiveField.PERIOD -> { sumFocusRequester.requestFocus() }
                else -> { sumFocusRequester.requestFocus() }
            }
        }
    }

    val handleMoveCursorUp: () -> Unit = remember {
        {
            when(taxUiState.activeField) {
                ActiveField.SUM -> { periodFocusRequester.requestFocus() }
                ActiveField.RATE -> { sumFocusRequester.requestFocus() }
                ActiveField.PERIOD -> { rateFocusRequester.requestFocus() }
                else -> { sumFocusRequester.requestFocus() }
            }
        }
    }

    val onCalculateSimpleDeposit: (Double,Double, Double) -> Unit = remember {
        { _,_,_ -> taxViewModel.simpleDeposit() }
    }

    val onCalculateCapitalDeposit: (Double,Double,Double) -> Unit = remember {
        { _,_,_ -> taxViewModel.capitalizedDeposit()}
    }

    val onSumInputChanged: (String) -> Unit = { newValue ->
        taxViewModel.updateInput(newValue)
        taxViewModel.setActiveField(ActiveField.SUM)
    }

    val onTaxInputChanged: (String) -> Unit = { newValue ->
        taxViewModel.updateInput(newValue)
        taxViewModel.setActiveField(ActiveField.RATE)
    }

    val onPeriodInputChanged: (String) -> Unit = { newValue ->
        taxViewModel.updateInput(newValue)
        taxViewModel.setActiveField(ActiveField.PERIOD)
    }

    val onActiveFieldChanged: (ActiveField) -> Unit = remember {
        {newActiveField ->
            taxUiState.activeField = newActiveField

            when (newActiveField) {
                ActiveField.SUM -> sumFocusRequester.requestFocus()
                ActiveField.RATE -> rateFocusRequester.requestFocus()
                ActiveField.PERIOD -> periodFocusRequester.requestFocus()
                else -> { /* При сбросе фокуса на NONE, не фокусируемся ни на чем */ }
            }
        }
    }

    SimpleDepRender(
        currentTotalAmount = taxUiState.totalAmount,
        sumInputValue = taxUiState.sumInput,
        rateInputValue = taxUiState.rateInput,
        periodInputValue = taxUiState.periodInput,
        activeField = taxUiState.activeField,
        onSumInputChanged = onSumInputChanged,
        onRateInputChanged = onTaxInputChanged,
        onPeriodInputChanged = onPeriodInputChanged,
        onActiveFieldChanged = onActiveFieldChanged,
        onCalculateSimpleDep = onCalculateSimpleDeposit,
        onCalculateCapitalDep = onCalculateCapitalDeposit,
        onNumberClick = handleNumberInput,
        onCommaClick = handleCommaClick,
        onDeleteClick = handleDeleteClick,
        onClearClick = handleClearClick,
        onMoveCursorDownClick = handleMoveCursorDown,
        onMoveCursorUpClick = handleMoveCursorUp,
        modifier = modifier,
        sumFocusRequester = sumFocusRequester,
        rateFocusRequester = rateFocusRequester,
        periodFocusRequester = periodFocusRequester
    )
}

@Preview(showBackground = true)
@Composable
fun ShowVATPreview() {
    val initialState = TaxUIState()
    val context = LocalContext.current
    val application = MockApplicationForPreview(context)
    val mockFactory = PreviewTaxViewModelFactory(application,initialState)

    FinHelperTheme {
        ShowDeposit(
            onShowBottomBar = {},
            finViewModelFactory = mockFactory
        )
    }
}