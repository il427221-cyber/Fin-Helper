package ru.myproject.finhelper.ui.taxes.property_tax

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
fun PropertyTax_Render(
    currentTaxAmount: Double,
    propertyInputValue: String, // Текущее значение поля "Имущество"
    areaInputValue: String, // Текущее значение поля "Площадь"
    taxInputValue: String, // Текущее значение поля "Ставка"
    shareInputValue: String, // Текущее значение поля "Доля"
    periodInputValue: String, // Текущее значение поля "Период"
    activeField: ActiveField,
    onPropertyInputChanged: (String) -> Unit, // Колбэк для изменения propertyInputValue
    onAreaInputChanged: (String) -> Unit, // Колбэк для изменения areaInputValue
    onTaxInputChanged: (String) -> Unit, // Колбэк для изменения taxInputValue
    onShareInputChanged: (String) -> Unit, // Колбэк для изменения shareInputValue
    onPeriodInputChanged: (String) -> Unit, // Колбэк для изменения periodInputValue
    onActiveFieldChanged: (ActiveField) -> Unit, // Колбэк для оповещения о смене активного поля
    onCalculateTaxClick: (property: Double, area: Double, tax: Double, share: Double, period: Double) -> Unit,
    onNumberClick: (String) -> Unit,
    onCommaClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onClearClick: () -> Unit,
    onMoveCursorDownClick: () -> Unit,
    onMoveCursorUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    propertyFocusRequester: FocusRequester,
    areaFocusRequester: FocusRequester,
    taxFocusRequester: FocusRequester,
    shareFocusRequester: FocusRequester,
    periodFocusRequester: FocusRequester

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

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    NumericInputField(
                        text = stringResource(R.string.cost_of_housing),
                        number = propertyInputValue,
                        onValueChange = onPropertyInputChanged,
                        onFocusGained = {onActiveFieldChanged(ActiveField.PROPERTY)},
                        focusRequester = propertyFocusRequester,
                        modifier = Modifier
                            .weight(0.6f)
                    )

                    NumericInputField(
                        text = stringResource(R.string.area_sq_m),
                        number = areaInputValue,
                        onValueChange = onAreaInputChanged,
                        onFocusGained = { onActiveFieldChanged(ActiveField.AREA) },
                        focusRequester = areaFocusRequester,
                        modifier = Modifier
                            .weight(0.4f)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                NumericInputField(
                    text = stringResource(R.string.rate),
                    number = taxInputValue,
                    onValueChange = onTaxInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.RATE) },
                    focusRequester = taxFocusRequester,
                )

                Spacer(modifier = Modifier.height(12.dp))

                NumericInputField(
                    text = stringResource(R.string.share),
                    number = shareInputValue,
                    onValueChange = onShareInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.SHARE) },
                    focusRequester = shareFocusRequester,
                )

                Spacer(modifier = Modifier.height(12.dp))

                NumericInputField(
                    text = stringResource(R.string.holding_period),
                    number = periodInputValue,
                    onValueChange = onPeriodInputChanged,
                    onFocusGained = { onActiveFieldChanged(ActiveField.PERIOD) },
                    focusRequester = periodFocusRequester,
                )

                Spacer(modifier = Modifier.height(12.dp))

                NumericOutputField(
                    text = stringResource(R.string.tax),
                    value = "%.2f".format(currentTaxAmount),
                    textHint = stringResource(R.string.rub),
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val property = propertyInputValue.toDoubleOrNull() ?: 0.0
                        val area = areaInputValue.toDoubleOrNull() ?: 0.0
                        val tax = taxInputValue.toDoubleOrNull() ?: 0.0
                        val share = shareInputValue.toDoubleOrNull() ?: 0.0
                        val period = periodInputValue.toDoubleOrNull() ?: 0.0

                        onCalculateTaxClick(property, area,tax, share, period)
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