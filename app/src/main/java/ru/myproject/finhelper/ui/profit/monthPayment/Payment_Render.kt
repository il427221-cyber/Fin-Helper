package ru.myproject.finhelper.ui.profit.monthPayment

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
import ru.myproject.finhelper.dto.profit_ui_state.ActiveField

@Composable
fun PaymentRendering(
    monthPayment: Double,
    sumInputValue: String,
    rateInputValue: String,
    periodInputValue: String,
    activeField: ActiveField,
    onSumInputChanged: (String) -> Unit,
    onRateInputChanged: (String) -> Unit,
    onPeriodInputChanged: (String) -> Unit,
    onActiveFieldChanged: (ActiveField) -> Unit,
    onCalculatePaymentClick: (Double, Double, Double) -> Unit,
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
                Spacer(modifier = Modifier.height(16.dp))

                NumericInputField(
                    text = stringResource(R.string.rate),
                    number = rateInputValue,
                    onValueChange = onRateInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.RATE) },
                    focusRequester = rateFocusRequester
                )
                Spacer(modifier = Modifier.height(16.dp))

                NumericInputField(
                    text = stringResource(R.string.period_months),
                    number = periodInputValue,
                    onValueChange = onPeriodInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.PERIOD) },
                    focusRequester = periodFocusRequester
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            NumericOutputField(
                text = stringResource(R.string.payment),
                value = "%.2f".format(monthPayment),
                textHint = stringResource(R.string.rub),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val sum = sumInputValue.toDoubleOrNull() ?: 0.0
                    val rate = rateInputValue.toDoubleOrNull() ?: 0.0
                    val period = periodInputValue.toDoubleOrNull() ?: 0.0
                    onCalculatePaymentClick(sum, rate, period)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.calculate_payment))
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