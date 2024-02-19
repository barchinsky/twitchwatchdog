package com.m.twitchwatchdog.dashboard.ui.topBar

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.m.twitchwatchdog.R
import com.m.twitchwatchdog.ui.theme.TwitchWatchdogTheme

@Composable
fun TopBar(
    notificationsEnabled: Boolean,
    syncJobEnabled: Boolean,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .windowInsetsPadding(WindowInsets.statusBars)
            .then(modifier)
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
            Icon(Icons.Filled.Settings, contentDescription = "Settings")
        }
    }
}

@Preview
@Composable
fun TopBarPreview() {
    TwitchWatchdogTheme {
        TopBar(
            notificationsEnabled = true,
            syncJobEnabled = true,
            onNotificationsClick = { /*TODO*/ },
            onSettingsClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        )
    }
}