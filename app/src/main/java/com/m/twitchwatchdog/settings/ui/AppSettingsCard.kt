package com.m.twitchwatchdog.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.m.twitchwatchdog.infrastructure.datasource.settings.model.AppSettings
import com.m.twitchwatchdog.ui.theme.TwitchWatchdogTheme

@Composable
fun AppSettingsCard(
    appSettings: AppSettings,
    modifier: Modifier = Modifier,
    onValueChanged: (Int, Int) -> Unit,
) {
    var value by remember {
        mutableStateOf(appSettings.checkStartHour.toFloat()..appSettings.checkEndHour.toFloat())
    }

    Column(modifier = Modifier.then(modifier)) {
        Column(Modifier.fillMaxWidth()) {
            Text(text = "Notify between ${value.start.toInt()}:00 and ${value.endInclusive.toInt()}:00")
            RangeSlider(
                value = value,
                onValueChange = { value = it },
                onValueChangeFinished = {
                    onValueChanged(
                        value.start.toInt(),
                        value.endInclusive.toInt()
                    )
                },
                steps = 23,
                valueRange = 0f..23f,
            )
        }
    }
}

@PreviewLightDark
@Composable
fun AppSettingsCardPreview() {
    TwitchWatchdogTheme {
        Surface {
            AppSettingsCard(
                appSettings = AppSettings(0, 23),
                onValueChanged = { _, _ -> },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}