package com.m.twitchwatchdog.settings.ui

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.m.twitchwatchdog.R
import com.m.twitchwatchdog.infrastructure.datasource.model.AppSettings
import com.m.shared.ui.theme.TwitchWatchdogTheme

@Composable
fun AppSettingsCard(
    appSettings: AppSettings,
    modifier: Modifier = Modifier,
    onValueChanged: (Int, Int) -> Unit,
) {
    val vibrationEffect = VibrationEffect.createOneShot(100L, 70)
    val vibrator = LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    var value by remember {
        mutableStateOf(appSettings.checkStartHour.toFloat()..appSettings.checkEndHour.toFloat())
    }

    Column(modifier = Modifier.then(modifier)) {
        Column(Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(
                    R.string.settings_notify_between,
                    value.start.toInt(),
                    value.endInclusive.toInt()
                )
            )
            RangeSlider(
                value = value,
                onValueChange = {
                    vibrator.vibrate(vibrationEffect)
                    value = it
                },
                onValueChangeFinished = {
                    onValueChanged(
                        value.start.toInt(),
                        value.endInclusive.toInt()
                    )
                },
                steps = 24,
                valueRange = 0f..24f,
            )
        }
    }
}

@PreviewLightDark
@Composable
fun AppSettingsCardPreview() {
    com.m.shared.ui.theme.TwitchWatchdogTheme {
        Surface {
            AppSettingsCard(
                appSettings = AppSettings.getDefault(),
                onValueChanged = { _, _ -> },
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}