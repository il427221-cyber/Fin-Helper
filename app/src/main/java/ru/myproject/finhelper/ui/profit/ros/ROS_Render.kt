package ru.myproject.finhelper.ui.profit.ros

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
fun ROSRendering(
    currentROSAmount: Double,
    incomeInputValue: String,
    profitInputValue: String,
    activeField: ActiveField,
    onIncomeInputChanged: (String) -> Unit,
    onProfitInputChanged: (String) -> Unit,
    onActiveFieldChanged: (ActiveField) -> Unit,
    onCalculateROSClick: (Double, Double) -> Unit,
    onNumberClick: (String) -> Unit,
    onCommaClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onClearClick: () -> Unit,
    onMoveCursorClick: () -> Unit,
    modifier: Modifier = Modifier,
    incomeFocusRequester: FocusRequester,
    profitFocusRequester: FocusRequester
) {
    Column(modifier = Modifier.fillMaxSize()) {
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
                    text = stringResource(R.string.revenue_rub),
                    number = incomeInputValue,
                    onValueChange = onIncomeInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.INCOME) },
                    focusRequester = incomeFocusRequester
                )
                Spacer(modifier = Modifier.height(16.dp))

                NumericInputField(
                    text = stringResource(R.string.profit_rub),
                    number = profitInputValue,
                    onValueChange = onProfitInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.PROFIT) },
                    focusRequester = profitFocusRequester
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
                text = stringResource(R.string.ROS_index),
                value = "%.0f".format(currentROSAmount),
                textHint = stringResource(R.string.percent),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val income = incomeInputValue.toDoubleOrNull() ?: 0.0
                    val profit = profitInputValue.toDoubleOrNull() ?: 0.0
                    onCalculateROSClick(income, profit)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.calculate_ros))
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