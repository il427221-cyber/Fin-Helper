package ru.myproject.finhelper.ui.numericfield

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
    fun NumericInputField(
    text: String,
    number: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onFocusGained: () -> Unit,
    focusRequester: FocusRequester
            ) {
                val interactionSource = remember { MutableInteractionSource() }
    // Используем TextFieldValue для управления текстом и курсором
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = number,
                selection = TextRange(number.length)
            )
        )
    }
    // LaunchedEffect синхронизирует внешнее 'number' состояние с внутренним 'textFieldValue'
    // Это критически важно для того, чтобы изменения извне (от клавиатуры) отображались в BasicTextField.
    LaunchedEffect(number) {
        if (textFieldValue.text != number) {
            val newCursorPosition = number.length // Курсор всегда в конце
            textFieldValue = textFieldValue.copy(text = number, selection = TextRange(newCursorPosition))
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = text, style = TextStyle(fontSize = 20.sp))
        Spacer(modifier = Modifier.width(12.dp))

        // Основной компонент для ввода текста и отображения курсора
        BasicTextField(
            value = textFieldValue, // Используем внутреннее состояние TextFieldValue
            onValueChange = { newValue ->
                // В этом примере мы просто передаем весь текст дальше
                val filtered = newValue.text.filter { it.isDigit() || it == '.' || it == ',' }
                val newText = filtered.replace(',', '.')
                // Обновляем внутреннее состояние
                textFieldValue = newValue.copy(text = newText)
                // Отправляем очищенное и отфильтрованное значение наверх
                onValueChange(newText)
            },
            singleLine = true,
            modifier = Modifier.weight(1f).focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.hasFocus) {
                        onFocusGained()
                        val currentText = textFieldValue.text
                        textFieldValue = textFieldValue.copy(selection = TextRange(currentText.length))
                    }
                },
            readOnly = true, // Блокировка системной клавиатуры
            interactionSource = interactionSource,
            // decorationBox для обертывания BasicTextField в стиль OutlinedTextField
            decorationBox = { innerTextField ->
                OutlinedTextField(
                    value = textFieldValue.text, // OutlinedTextField показывает текст из BasicTextField
                    onValueChange = { /* onValueChange здесь не используется, ввод через BasicTextField */ },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true, // Внешний OutlinedTextField только для чтения
                    singleLine = true,
                    interactionSource = interactionSource,
                    enabled = true, // Поле должно выглядеть активным
                )
            }
        )
    }
}