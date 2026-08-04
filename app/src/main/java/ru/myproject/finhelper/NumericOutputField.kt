package ru.myproject.finhelper

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumericOutputField(
    text:String,
    value: String,
    modifier: Modifier = Modifier,
    textHint: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center)
    {
        Text(text = text, style = TextStyle(fontSize = 20.sp))
        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = value,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            style = TextStyle(fontSize = 20.sp)
        )

        Spacer(modifier = Modifier.width(12.dp))
        Text(text = textHint, style = TextStyle(fontSize = 20.sp))
    }
}












