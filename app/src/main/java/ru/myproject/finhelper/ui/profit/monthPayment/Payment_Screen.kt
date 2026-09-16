package ru.myproject.finhelper.ui.profit.monthPayment

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
import ru.myproject.finhelper.viewmodel.ProfitViewModel
import ru.myproject.finhelper.dto.profit_ui_state.ActiveField
import ru.myproject.finhelper.dto.profit_ui_state.ProfitUiState
import ru.myproject.finhelper.ui.MockApplicationForPreview
import ru.myproject.finhelper.ui.profit.PreviewProfitViewModelFactory
import ru.myproject.finhelper.ui.theme.FinHelperTheme

@Composable
fun ShowPayment(
    onShowBottomBar: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    profitViewModelFactory: ViewModelProvider.Factory)
{
    val profitViewModel: ProfitViewModel = viewModel(factory = profitViewModelFactory)
    val context = LocalContext.current

    LaunchedEffect(profitViewModel) {
        profitViewModel.uiMessages.collect{message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
    LaunchedEffect(Unit) {
        onShowBottomBar(false)
    }

    val uiState by profitViewModel.uiState.collectAsState()
    val sumFocusRequester = remember { FocusRequester() }
    val rateFocusRequester = remember { FocusRequester() }
    val periodFocusRequester = remember { FocusRequester() }

    LaunchedEffect(uiState.activeField) {
        when (uiState.activeField) {
            ActiveField.SUM -> sumFocusRequester.requestFocus()
            ActiveField.RATE -> rateFocusRequester.requestFocus()
            ActiveField.PERIOD -> periodFocusRequester.requestFocus()
            else -> {
                sumFocusRequester.freeFocus()
                rateFocusRequester.freeFocus()
                periodFocusRequester.freeFocus()
            }
        }
    }

    val handleNumberInput: (String) -> Unit =  { number ->
        profitViewModel.appendNumberToActiveField(number) }

    val handleCommaClick: () -> Unit =  { profitViewModel.appendCommaToActiveField() }

    val handleDeleteClick: () -> Unit =  { profitViewModel.deleteLastCharFromActiveField() }

    val handleClearClick: () -> Unit =  {
        profitViewModel.clearAllFields()
        sumFocusRequester.requestFocus()
    }

    val handleMoveCursorDownClick: () -> Unit = remember {
        {
            when(uiState.activeField) {
                ActiveField.SUM -> { rateFocusRequester.requestFocus() }
                ActiveField.RATE -> { periodFocusRequester.requestFocus() }
                ActiveField.PERIOD -> { sumFocusRequester.requestFocus() }
                else -> { sumFocusRequester.requestFocus() }
            }
        }
    }

    val handleMoveCursorUpClick: () -> Unit = remember {
        {
            when(uiState.activeField) {
                ActiveField.SUM -> { periodFocusRequester.requestFocus() }
                ActiveField.RATE -> { sumFocusRequester.requestFocus() }
                ActiveField.PERIOD -> { rateFocusRequester.requestFocus() }
                else -> { sumFocusRequester.requestFocus() }
            }
        }
    }

    val onCalculatePayment: (Double,Double, Double) -> Unit = remember {
        { _, _, _ -> profitViewModel.calculateMonthPayment() }
    }

    val onSumInputChanged: (String) -> Unit = { newValue ->
        profitViewModel.updateInput(newValue)
        profitViewModel.setActiveField(ActiveField.SUM)
    }

    val onRateInputChanged: (String) -> Unit = { newValue ->
        profitViewModel.updateInput(newValue)
        profitViewModel.setActiveField(ActiveField.RATE)
    }

    val onPeriodInputChanged: (String) -> Unit = { newValue ->
        profitViewModel.updateInput(newValue)
        profitViewModel.setActiveField(ActiveField.PERIOD)
    }

    val onActiveFieldChanged: (ActiveField) -> Unit = remember {
        {newActiveField ->
            uiState.activeField = newActiveField

            when (newActiveField) {
                ActiveField.SUM -> sumFocusRequester.requestFocus()
                ActiveField.RATE-> rateFocusRequester.requestFocus()
                ActiveField.PERIOD -> periodFocusRequester.requestFocus()
                else -> {  }
            }
        }
    }
    PaymentRendering(
        monthPayment = uiState.monthPayment,
        sumInputValue = uiState.sumInput,
        rateInputValue = uiState.rateInput,
        periodInputValue = uiState.periodInput,
        activeField = uiState.activeField,
        onSumInputChanged = onSumInputChanged,
        onRateInputChanged = onRateInputChanged,
        onPeriodInputChanged = onPeriodInputChanged,
        onActiveFieldChanged = onActiveFieldChanged,
        onCalculatePaymentClick = onCalculatePayment,
        onNumberClick = handleNumberInput,
        onCommaClick = handleCommaClick,
        onDeleteClick = handleDeleteClick,
        onClearClick = handleClearClick,
        onMoveCursorDownClick = handleMoveCursorDownClick,
        onMoveCursorUpClick = handleMoveCursorUpClick,
        modifier = modifier,
        sumFocusRequester = sumFocusRequester,
        rateFocusRequester = rateFocusRequester,
        periodFocusRequester = periodFocusRequester
    )
}

@Preview(showBackground = true)
@Composable
fun ShowPaymentPreview() {
    val initialState = ProfitUiState()
    val context = LocalContext.current
    val application = MockApplicationForPreview(context)
    val mockFactory = PreviewProfitViewModelFactory(application,initialState)

    FinHelperTheme {
        ShowPayment(
            onShowBottomBar = {},
            modifier = Modifier,
            profitViewModelFactory = mockFactory
        )
    }
}