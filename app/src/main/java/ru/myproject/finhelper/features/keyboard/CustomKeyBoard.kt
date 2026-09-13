package ru.myproject.finhelper.features.keyboard

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
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.myproject.finhelper.R
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

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Button(onClick = { onNumberClick("1") }) { Text(stringResource(R.string.one)) }
            Button(onClick = { onNumberClick("2") }) { Text(stringResource(R.string.two)) }
            Button(onClick = { onNumberClick("3") }) { Text(stringResource(R.string.three)) }

            Button({ onDeleteClick() }) {
                Icon(Icons.Outlined.Backspace, contentDescription = stringResource(R.string.delete))
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Button(onClick = { onNumberClick("4") }) { Text(stringResource(R.string.four)) }
            Button(onClick = { onNumberClick("5") }) { Text(stringResource(R.string.five)) }
            Button(onClick = { onNumberClick("6") }) { Text(stringResource(R.string.six)) }

            Button({ onMoveCursorDownClick() }) {
                Icon(Icons.Outlined.ArrowDownward,
                    contentDescription = stringResource(R.string.move_cursor_down))
            }
        }
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Button(onClick = { onNumberClick("7") }) { Text(stringResource(R.string.seven)) }
            Button(onClick = { onNumberClick("8") }) { Text(stringResource(R.string.eight)) }
            Button(onClick = { onNumberClick("9") }) { Text(stringResource(R.string.nine)) }

            Button({ onMoveCursorUpClick() }) {
                Icon(Icons.Outlined.ArrowUpward,
                    contentDescription = stringResource(R.string.move_cursor_up))
            }

        }
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Button(onClick = { onNumberClick("0") }) { Text(stringResource(R.string.zero)) }
            Button(onClick = { onNumberClick("00") }) { Text(stringResource(R.string.double_zero)) }
            Button(onClick = { onCommaClick() }) { Text(stringResource(R.string.comma)) }

            Button({ onClearClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.my_light_red))
                ) {
                Icon(Icons.Outlined.Delete, contentDescription = stringResource(R.string.clear_all))
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