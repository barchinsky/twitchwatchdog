package com.m.twitchwatchdog.infrastructure.ui.switchRow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.m.twitchwatchdog.ui.theme.Green40
import com.m.twitchwatchdog.ui.theme.TwitchWatchdogTheme

@Composable
fun SwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = title)

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChanged,
            colors = SwitchDefaults.colors(checkedTrackColor = Green40)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun SwitchRowPreview() {
    TwitchWatchdogTheme {
        SwitchRow(title = "Notify when live", checked = false, onCheckedChanged = {}, modifier = Modifier.fillMaxWidth())
    }
}