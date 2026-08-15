package ru.myproject.finhelper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.myproject.finhelper.ui.theme.FinHelperTheme

@Composable
fun CustomKeyBoard(
    onNumberClick: (String) -> Unit,
    onCommaClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onClearClick: () -> Unit,
    onMoveCursorDownClick: () -> Unit,
    onMoveCursorUpClick: () -> Unit
) {

    Column(modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Button(onClick = { onNumberClick("1") }) { Text("1") }
            Button(onClick = { onNumberClick("2") }) { Text("2") }
            Button(onClick = { onNumberClick("3") }) { Text("3") }

            Button({ onDeleteClick() }) {
                Icon(Icons.Outlined.Clear, contentDescription = "Delete")
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Button(onClick = { onNumberClick("4") }) { Text("4") }
            Button(onClick = { onNumberClick("5") }) { Text("5") }
            Button(onClick = { onNumberClick("6") }) { Text("6") }

            Button({ onMoveCursorDownClick() }) {
                Icon(Icons.Outlined.ArrowDownward, contentDescription = "Move Cursor Down")
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Button(onClick = { onNumberClick("7") }) { Text("7") }
            Button(onClick = { onNumberClick("8") }) { Text("8") }
            Button(onClick = { onNumberClick("9") }) { Text("9") }

            Button({ onMoveCursorUpClick() }) {
                Icon(Icons.Outlined.ArrowUpward, contentDescription = "Move Cursor Up")
            }

        }
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Button(onClick = { onNumberClick("0") }) { Text("0") }
            Button(onClick = { onNumberClick("00") }) { Text("00") }
            Button(onClick = { onCommaClick() }) { Text(",") }

            Button({ onClearClick() }) {
                Icon(Icons.Outlined.Delete, contentDescription = "Clear all")
            }
        }
    }
}

    @Preview
    @Composable
    fun CustomKeyBoardPreview() {
        FinHelperTheme {
            CustomKeyBoard(
                onNumberClick = {},
                onCommaClick = {},
                onDeleteClick = {},
                onClearClick = {},
                onMoveCursorDownClick = {},
                onMoveCursorUpClick = {}
            )
        }
    }