package ru.myproject.finhelper.ui.deposit.complex

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.myproject.finhelper.R
import ru.myproject.finhelper.features.keyboard.CustomKeyBoard
import ru.myproject.finhelper.features.numericfield.NumericInputField
import ru.myproject.finhelper.features.numericfield.NumericOutputField
import ru.myproject.finhelper.dto.tax_ui_state.ActiveField

@Composable
fun ComplexDepRender(
    currentTotalAmount: Double,
    sumInputValue: String,
    rateInputValue: String,
    periodInputValue: String,
    quantityInputValue: String,
    activeField: ActiveField,
    onSumInputChanged: (String) -> Unit,
    onRateInputChanged: (String) -> Unit,
    onPeriodInputChanged: (String) -> Unit,
    onQuantityInputChanged: (String) -> Unit,
    onActiveFieldChanged: (ActiveField) -> Unit,
    onCalculateComplexDep: (sum: Double, rate: Double, period: Double, quantity: Double) -> Unit,
    onNumberClick: (String) -> Unit,
    onCommaClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onClearClick: () -> Unit,
    onMoveCursorDownClick: () -> Unit,
    onMoveCursorUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    sumFocusRequester: FocusRequester,
    rateFocusRequester: FocusRequester,
    periodFocusRequester: FocusRequester,
    quantityFocusRequester: FocusRequester,
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
                    text = stringResource(R.string.sum_rub),
                    number = sumInputValue,
                    onValueChange = onSumInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.SUM) },
                    focusRequester = sumFocusRequester
                )
                Spacer(modifier = Modifier.height(8.dp))

                NumericInputField(
                    text = stringResource(R.string.rate),
                    number = rateInputValue,
                    onValueChange = onRateInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.RATE) },
                    focusRequester = rateFocusRequester
                )

                Spacer(modifier = Modifier.height(8.dp))

                NumericInputField(
                    text = stringResource(R.string.period_in_years),
                    number = periodInputValue,
                    onValueChange = onPeriodInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.PERIOD) },
                    focusRequester = periodFocusRequester
                )

                Spacer(modifier = Modifier.height(8.dp))

                NumericInputField(
                    text = stringResource(R.string.quantity_of_periods_months_per_year),
                    number = quantityInputValue,
                    onValueChange = onQuantityInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.QUANTITY) },
                    focusRequester = quantityFocusRequester
                )

                Spacer(modifier = Modifier.height(8.dp))

                NumericOutputField(
                    text = stringResource(R.string.total_sum),
                    value = "%.2f".format(currentTotalAmount),
                    textHint = stringResource(R.string.rub),
                    )

                Button(
                    onClick = {
                        val sum = sumInputValue.toDoubleOrNull() ?: 0.0
                        val rate = rateInputValue.toDoubleOrNull() ?: 0.0
                        val period = periodInputValue.toDoubleOrNull() ?: 0.0
                        val quantity = quantityInputValue.toDoubleOrNull() ?: 0.0
                        onCalculateComplexDep(sum, rate, period, quantity)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.calculate_sum_with_period_capitalization))
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
                onMoveCursorDownClick = onMoveCursorDownClick,
                onMoveCursorUpClick = onMoveCursorUpClick
            )
        }
    }
}