package ru.myproject.finhelper.ui.taxes.vat

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
import ru.myproject.finhelper.viewmodel.TaxViewModel
import ru.myproject.finhelper.dto.tax_ui_state.ActiveField
import ru.myproject.finhelper.dto.tax_ui_state.TaxUIState
import ru.myproject.finhelper.ui.MockApplicationForPreview
import ru.myproject.finhelper.ui.taxes.PreviewTaxViewModelFactory
import ru.myproject.finhelper.ui.theme.FinHelperTheme

@Composable
fun ShowVATAdded(onShowBottomBar: (Boolean) -> Unit, modifier: Modifier = Modifier,
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
    val taxFocusRequester = remember { FocusRequester() }

    LaunchedEffect(taxViewModel) {
        when(taxUiState.activeField) {
            ActiveField.SUM -> sumFocusRequester.requestFocus()
            ActiveField.TAX -> taxFocusRequester.requestFocus()
            else -> {
                sumFocusRequester.freeFocus()
                taxFocusRequester.freeFocus()
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

    val handleMoveCursorClick: () -> Unit = remember {
        {
            when(taxUiState.activeField) {
                ActiveField.SUM -> { taxFocusRequester.requestFocus() }
                ActiveField.TAX -> { sumFocusRequester.requestFocus() }
                else -> { sumFocusRequester.requestFocus() }
            }
        }
    }

    val onCalculateVATButtonClick: (Double,Double) -> Unit = remember {
        { _,_ -> taxViewModel.calculate_VAT_And_Total() }
    }

    val onExtractVATButtonClick: (Double,Double) -> Unit = remember {
        { _,_ -> taxViewModel.extract_VAT_And_Total() }
    }

    val onSumInputChanged: (String) -> Unit = { newValue ->
        taxViewModel.updateInput(newValue)
        taxViewModel.setActiveField(ActiveField.SUM)
    }

    val onTaxInputChanged: (String) -> Unit = { newValue ->
        taxViewModel.updateInput(newValue)
        taxViewModel.setActiveField(ActiveField.TAX)
    }

    val onActiveFieldChanged: (ActiveField) -> Unit = remember {
        {newActiveField ->
            taxUiState.activeField = newActiveField

            when (newActiveField) {
                ActiveField.SUM -> sumFocusRequester.requestFocus()
                ActiveField.TAX -> taxFocusRequester.requestFocus()
                else -> { /* При сбросе фокуса на NONE, не фокусируемся ни на чем */ }
            }
        }
    }

    VATRendering(
        currentVatAmount = taxUiState.taxAmount,
        currentTotalAmount = taxUiState.totalAmount,
        sumInputValue = taxUiState.sumInput,
        taxInputValue = taxUiState.taxInput,
        activeField = taxUiState.activeField,
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

@Preview(showBackground = true)
@Composable
fun ShowVATPreview() {
    val initialState = TaxUIState()
    val context = LocalContext.current
    val application = MockApplicationForPreview(context)
    val mockFactory = PreviewTaxViewModelFactory(application,initialState)

    FinHelperTheme {
        ShowVATAdded(
            onShowBottomBar = {},
            finViewModelFactory = mockFactory
        )
    }
}