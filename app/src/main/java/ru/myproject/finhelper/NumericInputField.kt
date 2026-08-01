package ru.myproject.finhelper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumericInputField(
    text:String,
    number: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textHint: String,
    onFocusGained:() -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround)
    {
        Text(text = text, style = TextStyle(fontSize = 24.sp), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(12.dp))

        OutlinedTextField(value = number,
            readOnly = false,
            onValueChange = { newNumber ->
                val filteredValue = newNumber.filter { it.isDigit() || it == '.' || it == ','}
                onValueChange(filteredValue.replace(',', '.'))
            },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,

            modifier = Modifier
                .height(36.dp)
                .weight(1f)
                .onFocusChanged{focusState ->
                    if(focusState.isFocused) onFocusGained()
                }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = textHint, style = TextStyle(fontSize = 24.sp))

    }
}