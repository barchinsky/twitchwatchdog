package com.m.twitchwatchdog.dashboard.ui.topBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.m.twitchwatchdog.R
import com.m.twitchwatchdog.infrastructure.datasource.settings.model.AppSettings
import com.m.twitchwatchdog.settings.ui.AppSettingsCard
import com.m.twitchwatchdog.ui.theme.TwitchWatchdogTheme

@Composable
fun TopBar(
    notificationsEnabled: Boolean,
    appSettings: AppSettings,
    settingsVisible: Boolean,
    syncJobEnabled: Boolean,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onNotifyRangeChanged: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val settingsIconRotation by animateFloatAsState(
        targetValue = 0f.takeIf { settingsVisible } ?: 90f,
        label = "Settings icon animation"
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .windowInsetsPadding(WindowInsets.statusBars)
            .animateContentSize()
            .then(modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.ic_notifications_active_24.takeIf { notificationsEnabled }
                        ?: R.drawable.ic_notifications_off_24
                ),
                contentDescription = "Notifications enabled",
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier
                    .background(Color.Green.takeIf { syncJobEnabled } ?: Color.Red, CircleShape)
                    .size(10.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onSettingsClick() }
                        .rotate(settingsIconRotation)
                )
            }
        }
        AnimatedVisibility(visible = settingsVisible) {
            Column {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(1.dp)
                        .background(Color.Green)
                )
                AppSettingsCard(
                    appSettings = appSettings,
                    onValueChanged = onNotifyRangeChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun TopBarPreview() {
    TwitchWatchdogTheme {
        Surface {
            TopBar(
                notificationsEnabled = true,
                settingsVisible = true,
                appSettings = AppSettings(0, 23),
                syncJobEnabled = true,
                onNotificationsClick = { /*TODO*/ },
                onSettingsClick = { /*TODO*/ },
                onNotifyRangeChanged = { _, _ -> },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}