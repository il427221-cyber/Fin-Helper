package ru.myproject.finhelper.ui.profit.roi

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.myproject.finhelper.viewmodel.ProfitViewModel
import ru.myproject.finhelper.dto.profit_ui_state.ActiveField

@Composable
fun ShowROI(
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

    val incomeFocusRequester = remember { FocusRequester() }
    val expensesFocusRequester = remember { FocusRequester() }

    LaunchedEffect(uiState.activeField) {
        when (uiState.activeField) {
            ActiveField.INCOME -> incomeFocusRequester.requestFocus()
            ActiveField.EXPENSES -> expensesFocusRequester.requestFocus()
            ActiveField.NONE -> {
                // Очистка фокуса, если нужно
                incomeFocusRequester.freeFocus()
                expensesFocusRequester.freeFocus()
            }
        }
    }

    val handleNumberInput: (String) -> Unit =  { number ->
        profitViewModel.appendNumberToActiveField(number) }

    val handleCommaClick: () -> Unit =  { profitViewModel.appendCommaToActiveField() }

    val handleDeleteClick: () -> Unit =  { profitViewModel.deleteLastCharFromActiveField() }

    val handleClearClick: () -> Unit =  {
        profitViewModel.clearInputFields()
        profitViewModel.clearOutputFields()
        uiState.activeField = ActiveField.INCOME
    }

    val handleMoveCursorClick: () -> Unit = remember {
        {
            when(uiState.activeField) {
                ActiveField.INCOME -> { expensesFocusRequester.requestFocus() }
                ActiveField.EXPENSES -> { incomeFocusRequester.requestFocus() }
                ActiveField.NONE -> { incomeFocusRequester.requestFocus() }
            }
        }
    }

    val onCalculateROIButtonClick: (Double,Double) -> Unit = remember {
        { _, _ -> profitViewModel.calculateROI() }
    }

    val onIncomeInputChanged: (String) -> Unit = { newValue ->
        profitViewModel.updateIncomeInput(newValue)
        profitViewModel.setActiveField(ActiveField.INCOME)
    }

    val onExpensesInputChanged: (String) -> Unit = { newValue ->
        profitViewModel.updateExpensesInput(newValue)
        profitViewModel.setActiveField(ActiveField.EXPENSES)
    }

    val onActiveFieldChanged: (ActiveField) -> Unit = remember {
        {newActiveField ->
            uiState.activeField = newActiveField

            when (newActiveField) {
                ActiveField.INCOME -> incomeFocusRequester.requestFocus()
                ActiveField.EXPENSES -> expensesFocusRequester.requestFocus()
                ActiveField.NONE -> {  }
            }
        }
    }
    ROIRendering(
        currentROIAmount = uiState.roiIndex,
        incomeInputValue = uiState.incomeInput,
        expensesInputValue = uiState.expensesInput,
        activeField = uiState.activeField,
        onIncomeInputChanged = onIncomeInputChanged,
        onExpensesInputChanged = onExpensesInputChanged,
        onActiveFieldChanged = onActiveFieldChanged,
        onCalculateROIClick = onCalculateROIButtonClick,
        onNumberClick = handleNumberInput,
        onCommaClick = handleCommaClick,
        onDeleteClick = handleDeleteClick,
        onClearClick = handleClearClick,
        onMoveCursorClick = handleMoveCursorClick,
        modifier = modifier,
        incomeFocusRequester = incomeFocusRequester,
        expensesFocusRequester = expensesFocusRequester
    )
}

