package ru.myproject.finhelper.ui.taxes.income_tax

import android.app.Application
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
import ru.myproject.finhelper.dto.tax_ui_state.ActiveField
import ru.myproject.finhelper.dto.tax_ui_state.TaxUIState
import ru.myproject.finhelper.ui.MockApplicationForPreview
import ru.myproject.finhelper.ui.taxes.PreviewTaxViewModelFactory
import ru.myproject.finhelper.ui.theme.FinHelperTheme
import ru.myproject.finhelper.viewmodel.TaxViewModel

@Composable
fun ShowPersonalTax(onShowBottomBar: (Boolean) -> Unit, modifier: Modifier = Modifier,
                    finViewModelFactory: ViewModelProvider.Factory)
{
    val context = LocalContext.current

    val taxViewModel: TaxViewModel = viewModel(factory = finViewModelFactory)

    LaunchedEffect(taxViewModel) {
        taxViewModel.uiMessages.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        onShowBottomBar(false)
    }

    val taxUiState by taxViewModel.taxUiState.collectAsState()

    val sumFocusRequester = remember { FocusRequester() }
    val deductionFocusRequester = remember { FocusRequester() }
    val taxFocusRequester = remember { FocusRequester() }

    LaunchedEffect(taxViewModel) {
        when(taxUiState.activeField) {
            ActiveField.SUM -> sumFocusRequester.requestFocus()
            ActiveField.DEDUCTION -> deductionFocusRequester.requestFocus()
            ActiveField.TAX -> taxFocusRequester.requestFocus()
            else-> {
                sumFocusRequester.freeFocus()
                taxFocusRequester.freeFocus()
            }
        }
    }

    val handleNumberInput: (String) -> Unit =  {number ->
        taxViewModel.appendNumberToActiveField(number)
    }

    val handleCommaClick: () -> Unit =  {taxViewModel.appendCommaToActiveField()}

    val handleDeleteClick: () -> Unit =  {taxViewModel.deleteLastCharFromActiveField()}

    val handleClearClick: () -> Unit = {
        taxViewModel.clearAllFields()
        sumFocusRequester.requestFocus()
    }


    val handleMoveCursorDownClick: () -> Unit = remember {
        {
            when (taxUiState.activeField) {
                ActiveField.SUM -> { deductionFocusRequester.requestFocus() }
                ActiveField.DEDUCTION -> { taxFocusRequester.requestFocus() }
                ActiveField.TAX -> { sumFocusRequester.requestFocus() }
                else -> { sumFocusRequester.requestFocus() }
            }
        }
    }

    val handleMoveCursorUpClick: () -> Unit = remember {
        {
            when (taxUiState.activeField) {
                ActiveField.SUM -> { taxFocusRequester.requestFocus() }
                ActiveField.DEDUCTION -> { sumFocusRequester.requestFocus() }
                ActiveField.TAX -> { deductionFocusRequester.requestFocus() }
                else -> { sumFocusRequester.requestFocus() }
            }
        }
    }

    val onCalculateButtonClick: (Double, Double, Double) -> Unit = remember {
        { _, _, _ -> taxViewModel.calculate_PersonalTax() }
    }

    val onSumInputChanged: (String) -> Unit = { newValue ->
        taxViewModel.updateInput(newValue)
        taxViewModel.setActiveField(ActiveField.SUM)
    }

    val onDeductionInputChanged: (String) -> Unit = { newValue ->
        taxViewModel.updateInput(newValue)
        taxViewModel.setActiveField(ActiveField.DEDUCTION)
    }

    val onTaxInputChanged: (String) -> Unit = { newValue ->
        taxViewModel.updateInput(newValue)
        taxViewModel.setActiveField(ActiveField.TAX)
    }

    val onActiveFieldChanged: (ActiveField) -> Unit = remember {
        { newActiveField ->
            taxUiState.activeField = newActiveField

            when (newActiveField) {
                ActiveField.SUM -> sumFocusRequester.requestFocus()
                ActiveField.DEDUCTION -> deductionFocusRequester.requestFocus()
                ActiveField.TAX -> taxFocusRequester.requestFocus()
                else -> {  }
            }
        }
    }

    PersonalTax_Render(
        currentTaxAmount = taxUiState.taxAmount,
        currentTotalAmount = taxUiState.totalAmount,
        sumInputValue = taxUiState.sumInput,
        deductionInputValue = taxUiState.deductionInput,
        taxInputValue = taxUiState.taxInput,
        activeField = taxUiState.activeField,
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

@Preview(showBackground = true)
@Composable
fun PersonalTaxPreview() {
    val initialState = TaxUIState()
    val context = LocalContext.current
    val application = MockApplicationForPreview(context)
    val mockFactory = PreviewTaxViewModelFactory(application,initialState)

    FinHelperTheme {
        ShowPersonalTax(
            onShowBottomBar = {},
            finViewModelFactory = mockFactory
        )
    }
}