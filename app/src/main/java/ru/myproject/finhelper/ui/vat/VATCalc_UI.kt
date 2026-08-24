package ru.myproject.finhelper.ui.vat

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
import ru.myproject.finhelper.ui.keyboard.CustomKeyBoard
import ru.myproject.finhelper.ui.numericfield.NumericInputField
import ru.myproject.finhelper.ui.numericfield.NumericOutputField

enum class ActiveField {
    SUM, TAX, NONE
}
@Composable
fun CalculatorDisplay(
    currentVatAmount: Double,
    currentTotalAmount: Double,
    sumInputValue: String, // Текущее значение поля "Сумма"
    taxInputValue: String, // Текущее значение поля "Ставка"
    activeField: ActiveField,
    onSumInputChanged: (String) -> Unit, // Колбэк для изменения sumInputValue
    onTaxInputChanged: (String) -> Unit, // Колбэк для изменения taxInputValue
    onActiveFieldChanged: (ActiveField) -> Unit, // Колбэк для оповещения о смене активного поля
    onCalculateVATClick: (sum: Double, tax: Double) -> Unit,
    onExtractVATClick: (sum: Double, tax: Double) -> Unit,
    onNumberClick: (String) -> Unit,
    onCommaClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onClearClick: () -> Unit,
    onMoveCursorClick: () -> Unit,
    modifier: Modifier = Modifier,
    sumFocusRequester: FocusRequester,
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
                    text = "Сумма\nруб.:",
                    number = sumInputValue,
                    onValueChange = onSumInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.SUM) },
                    focusRequester = sumFocusRequester
                )

                    Spacer(modifier = Modifier.height(16.dp))

                NumericInputField(
                    text = "Ставка\n%:",
                    number = taxInputValue,
                    onValueChange = onTaxInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.TAX) },
                    focusRequester = taxFocusRequester
                )

                    Spacer(modifier = Modifier.height(16.dp))

                NumericOutputField(
                    text = "НДС:",
                    value = "%.2f".format(currentVatAmount),
                    textHint = "руб.",
                )

                    Spacer(modifier = Modifier.height(16.dp))

                NumericOutputField(
                    text = "Итоговая\n сумма:",
                    value = "%.2f".format(currentTotalAmount),
                    textHint = "руб.",

                    )

                    Button(
                        onClick = {
                            val sum = sumInputValue.toDoubleOrNull() ?: 0.0
                            val tax = taxInputValue.toDoubleOrNull() ?: 0.0
                            onCalculateVATClick(sum, tax)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Начислить НДС")
                    }

                    Button(
                    onClick = {
                        val sum = sumInputValue.toDoubleOrNull() ?: 0.0
                        val tax = taxInputValue.toDoubleOrNull() ?: 0.0
                        onExtractVATClick(sum, tax)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Выделить НДС")
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


