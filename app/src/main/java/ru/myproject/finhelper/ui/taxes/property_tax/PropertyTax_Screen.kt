package ru.myproject.finhelper.ui.taxes.property_tax

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
import ru.myproject.finhelper.ui.PreviewTaxViewModelFactory
import ru.myproject.finhelper.ui.theme.FinHelperTheme
import ru.myproject.finhelper.viewmodel.TaxViewModel

@Composable
fun ShowPropertyTax(onShowBottomBar: (Boolean) -> Unit, modifier: Modifier = Modifier,
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
    val propertyFocusRequester = remember { FocusRequester() }
    val areaFocusRequester = remember { FocusRequester() }
    val taxFocusRequester = remember { FocusRequester() }
    val shareFocusRequester = remember { FocusRequester() }
    val periodFocusRequester = remember { FocusRequester() }

    LaunchedEffect(taxViewModel) {
       when(taxUiState.activeField) {
           ActiveField.PROPERTY -> propertyFocusRequester.requestFocus()
           ActiveField.AREA -> areaFocusRequester.requestFocus()
           ActiveField.RATE -> taxFocusRequester.requestFocus()
           ActiveField.SHARE -> shareFocusRequester.requestFocus()
           ActiveField.PERIOD -> periodFocusRequester.requestFocus()
           else -> {
               propertyFocusRequester.freeFocus()
               areaFocusRequester.freeFocus()
               taxFocusRequester.freeFocus()
               shareFocusRequester.freeFocus()
               periodFocusRequester.freeFocus()
           }
       }
    }

    val handleNumberInput: (String) -> Unit = {number ->
        taxViewModel.appendNumberToActiveField(number)
    }

    val handleCommaClick: () -> Unit =  { taxViewModel.appendCommaToActiveField() }

    val handleDeleteClick: () -> Unit =  { taxViewModel.deleteLastCharFromActiveField() }

    val handleClearClick: () -> Unit =  {
        taxViewModel.clearAllFields()
        propertyFocusRequester.requestFocus()
    }

    val handleMoveCursorDownClick: () -> Unit = remember {
        {
            when (taxUiState.activeField) {
                ActiveField.PROPERTY -> { areaFocusRequester.requestFocus() }
                ActiveField.AREA -> { taxFocusRequester.requestFocus() }
                ActiveField.RATE -> { shareFocusRequester.requestFocus() }
                ActiveField.SHARE -> { periodFocusRequester.requestFocus() }
                else -> { propertyFocusRequester.requestFocus() }
            }
        }
    }

    val handleMoveCursorUpClick: () -> Unit = remember {
        {
            when (taxUiState.activeField) {
                ActiveField.PROPERTY -> { periodFocusRequester.requestFocus() }
                ActiveField.AREA -> { propertyFocusRequester.requestFocus() }
                ActiveField.RATE -> { areaFocusRequester.requestFocus() }
                ActiveField.SHARE -> { taxFocusRequester.requestFocus() }
                ActiveField.PERIOD -> { shareFocusRequester.requestFocus() }
                else -> { propertyFocusRequester.requestFocus() }
            }
        }
    }


    val onCalculateButtonClick: (Double, Double, Double,Double,Double) -> Unit = remember {
        { _, _, _, _, _ -> taxViewModel.calculate_PropertyTax() }
    }

    val onPropertyInputChanged: (String) -> Unit = { newValue ->
        taxViewModel.updateInput(newValue)
        taxViewModel.setActiveField(ActiveField.PROPERTY)
    }

    val onAreaInputChanged: (String) -> Unit = { newValue ->
        taxViewModel.updateInput(newValue)
        taxViewModel.setActiveField(ActiveField.AREA)
    }

    val onTaxInputChanged: (String) -> Unit = { newValue ->
        taxViewModel.updateInput(newValue)
        taxViewModel.setActiveField(ActiveField.RATE)
    }

    val onShareInputChanged: (String) -> Unit = { newValue ->
        taxViewModel.updateInput(newValue)
        taxViewModel.setActiveField(ActiveField.SHARE)
    }

    val onPeriodInputChanged: (String) -> Unit = { newValue ->
        taxViewModel.updateInput(newValue)
        taxViewModel.setActiveField(ActiveField.PERIOD)
    }

    val onActiveFieldChanged: (ActiveField) -> Unit = remember {
        { newActiveField ->
            taxUiState.activeField = newActiveField

            when (newActiveField) {
                ActiveField.PROPERTY -> propertyFocusRequester.requestFocus()
                ActiveField.AREA -> areaFocusRequester.requestFocus()
                ActiveField.RATE -> taxFocusRequester.requestFocus()
                ActiveField.SHARE -> shareFocusRequester.requestFocus()
                ActiveField.PERIOD -> periodFocusRequester.requestFocus()
                else -> {  }
            }
        }
    }

    PropertyTax_Render(
        currentTaxAmount = taxUiState.taxAmount,
        propertyInputValue = taxUiState.propertyInput,
        areaInputValue = taxUiState.areaInput,
        taxInputValue = taxUiState.rateInput,
        shareInputValue = taxUiState.shareInput,
        periodInputValue = taxUiState.periodInput,
        activeField = taxUiState.activeField,
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

@Preview(showBackground = true)
@Composable
fun PropertyTaxPreview() {
    val initialState = TaxUIState()
    val context = LocalContext.current
    val application = MockApplicationForPreview(context)
    val mockFactory = PreviewTaxViewModelFactory(application,initialState)

    FinHelperTheme {
        ShowPropertyTax(
            onShowBottomBar = {},
            finViewModelFactory = mockFactory
        )
    }
}