package ru.myproject.finhelper.features.numericfield

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewInputField(
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
        Text(text = text, style = TextStyle(fontSize = 14.sp))
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
            modifier = Modifier
                .weight(1f).focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.hasFocus) {
                        onFocusGained()
                        val currentText = textFieldValue.text
                        textFieldValue = textFieldValue.copy(selection = TextRange(currentText.length))
                    }
                },
            readOnly = true, // Блокировка системной клавиатуры
            interactionSource = interactionSource,
            textStyle = LocalTextStyle.current.copy (
                fontSize = 16.sp,
                lineHeight = 16.sp,
            ),

            // decorationBox для обертывания BasicTextField в стиль OutlinedTextField
            decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            // Рамка, имитирующая OutlinedTextField
                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)) // Настройте цвет и радиус
                            // Внутренние отступы для контента внутри рамки
                            // Важно: эти отступы должны быть меньше, чтобы текст помещался в 32.dp
                            .padding(horizontal = 8.dp, vertical = 12.dp) // <-- НАСТРОЙТЕ ЭТИ ЗНАЧЕНИЯ!
                    ) {

                        // *** Ключевой момент 4: Вызываем innerTextField() для отображения содержимого BasicTextField ***
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                //.padding(top = if (text.isNotEmpty()) 8.dp else 0.dp) // Отступ для текста, если есть лейбл
                        ) {
                            innerTextField() // <-- Здесь BasicTextField отрисовывает свой текст
                        }
                    }
            }

        )
    }
}