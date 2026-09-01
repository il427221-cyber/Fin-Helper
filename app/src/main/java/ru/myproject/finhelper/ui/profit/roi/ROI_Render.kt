package ru.myproject.finhelper.ui.profit.roi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import ru.myproject.finhelper.features.keyboard.CustomKeyBoard
import ru.myproject.finhelper.features.numericfield.NumericInputField
import ru.myproject.finhelper.features.numericfield.NumericOutputField
import ru.myproject.finhelper.dto.profit_ui_state.ActiveField

@Composable
fun ROIRendering(
    currentROIAmount: Double,
    incomeInputValue: String, // Текущее значение поля "Сумма"
    expensesInputValue: String, // Текущее значение поля "Ставка"
    activeField: ActiveField,
    onIncomeInputChanged: (String) -> Unit, // Колбэк для изменения sumInputValue
    onExpensesInputChanged: (String) -> Unit, // Колбэк для изменения taxInputValue
    onActiveFieldChanged: (ActiveField) -> Unit, // Колбэк для оповещения о смене активного поля
    onCalculateROIClick: (Double, Double) -> Unit,
    onNumberClick: (String) -> Unit,
    onCommaClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onClearClick: () -> Unit,
    onMoveCursorClick: () -> Unit,
    modifier: Modifier = Modifier,
    incomeFocusRequester: FocusRequester,
    expensesFocusRequester: FocusRequester
) {
    Column(modifier = Modifier.fillMaxSize()) {// включает 2 Box
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                NumericInputField(
                    text = "Доходы\nруб.:",
                    number = incomeInputValue,
                    onValueChange = onIncomeInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.INCOME) },
                    focusRequester = incomeFocusRequester
                )
                Spacer(modifier = Modifier.height(16.dp))

                NumericInputField(
                    text = "Расходы\nруб.:",
                    number = expensesInputValue,
                    onValueChange = onExpensesInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.EXPENSES) },
                    focusRequester = expensesFocusRequester
                )

                Spacer(modifier = Modifier.height(16.dp))

                NumericOutputField(
                    text = "ROI:",
                    value = "%.0f".format(currentROIAmount),
                    textHint = "%",
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val income = incomeInputValue.toDoubleOrNull() ?: 0.0
                        val expenses = expensesInputValue.toDoubleOrNull() ?: 0.0
                        onCalculateROIClick(income, expenses)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Рассчитать ROI")
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            CustomKeyBoard(
                onNumberClick = onNumberClick,
                onCommaClick = onCommaClick,
                onDeleteClick = onDeleteClick,
                onClearClick = onClearClick,
                onMoveCursorDownClick = onMoveCursorClick,
                onMoveCursorUpClick = onMoveCursorClick
            )
        }
    }
}