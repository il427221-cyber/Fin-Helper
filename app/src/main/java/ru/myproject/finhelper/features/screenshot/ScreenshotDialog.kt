package ru.myproject.finhelper.features.screenshot

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.myproject.finhelper.ui.theme.FinHelperTheme

@Composable
fun ScreenShotActionDialog(
    onDismissRequest:() -> Unit,
    onSaveClick:() -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Скриншот готов") },
        text =  { Text("Что вы хотите сделать со скриншотом?")},
        confirmButton = {
            TextButton(onClick = onSaveClick) { Text("Сохранить в галерею") }
        },
        dismissButton = {
            TextButton(onClick = onShareClick) { Text("Поделиться") }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun ScreenShotActionDialogPreview() {
    FinHelperTheme() {
        ScreenShotActionDialog(
            onDismissRequest = {},
            onSaveClick = {},
            onShareClick = {},
            modifier = Modifier
        )
    }
}

