package ru.myproject.finhelper.ui.taxes.income_tax

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
fun PersonalTax_Render(
    currentTaxAmount: Double,
    currentTotalAmount: Double,
    sumInputValue: String, // Текущее значение поля "Сумма"
    deductionInputValue: String, // Текущее значение поля "Вычеты"
    taxInputValue: String, // Текущее значение поля "Ставка"
    activeField: ActiveField,
    onSumInputChanged: (String) -> Unit, // Колбэк для изменения sumInputValue
    onDeductionInputChanged: (String) -> Unit, // Колбэк для изменения deductionInputValue
    onTaxInputChanged: (String) -> Unit, // Колбэк для изменения taxInputValue
    onActiveFieldChanged: (ActiveField) -> Unit, // Колбэк для оповещения о смене активного поля
    onCalculateTaxClick: (sum: Double, deduction: Double, tax: Double) -> Unit,
    onNumberClick: (String) -> Unit,
    onCommaClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onClearClick: () -> Unit,
    onMoveCursorDownClick: () -> Unit,
    onMoveCursorUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    sumFocusRequester: FocusRequester,
    deductionFocusRequester: FocusRequester,
    taxFocusRequester: FocusRequester
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
                    onFocusGained = {onActiveFieldChanged(ActiveField.SUM)},
                    focusRequester = sumFocusRequester
                )

                Spacer(modifier = Modifier.height(16.dp))

                NumericInputField(
                    text = stringResource(R.string.deduction_rub),
                    number = deductionInputValue,
                    onValueChange = onDeductionInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.DEDUCTION) },
                    focusRequester = deductionFocusRequester
                )

                Spacer(modifier = Modifier.height(16.dp))

                NumericInputField(
                    text = stringResource(R.string.rate),
                    number = taxInputValue,
                    onValueChange = onTaxInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.TAX) },
                    focusRequester = taxFocusRequester
                )

                Spacer(modifier = Modifier.height(16.dp))

                NumericOutputField(
                    text = stringResource(R.string.personal_income_tax),
                    value = "%.2f".format(currentTaxAmount),
                    textHint = stringResource(R.string.rub),
                )

                Spacer(modifier = Modifier.height(16.dp))

                NumericOutputField(
                    text = stringResource(R.string.sum_after_deduction),
                    value = "%.2f".format(currentTotalAmount),
                    textHint = stringResource(R.string.rub),
                    )

                Button(
                    onClick = {
                        val sum = sumInputValue.toDoubleOrNull() ?: 0.0
                        val deduction = deductionInputValue.toDoubleOrNull() ?: 0.0
                        val tax = taxInputValue.toDoubleOrNull() ?: 0.0
                        onCalculateTaxClick(sum, deduction,tax)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.calculate))
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
