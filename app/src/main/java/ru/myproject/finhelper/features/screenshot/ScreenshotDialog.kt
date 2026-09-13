package ru.myproject.finhelper.features.screenshot

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.myproject.finhelper.R
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
        title = { Text(stringResource(R.string.Screenshot_is_ready)) },
        text =  { Text(stringResource(R.string.Actions_with_a_screenshot))},
        confirmButton = {
            TextButton(onClick = onSaveClick) { Text(stringResource(R.string.Save_to_gallery)) }
        },
        dismissButton = {
            TextButton(onClick = onShareClick) { Text(stringResource(R.string.Share)) }
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

