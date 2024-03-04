package com.m.twitchwatchdog.dashboard.channelsList

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.m.twitchwatchdog.dashboard.channelCard.ChannelCard
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.ui.topBar.TopBar
import com.m.twitchwatchdog.infrastructure.datasource.settings.model.AppSettings
import com.m.twitchwatchdog.ui.swipeToDismiss.SwipeToDismissRow

@Composable
fun ChannelsList(
    channels: List<ChannelInfo>,
    appSettings: AppSettings,
    syncJobRunning: Boolean,
    onChannelClicked: (ChannelInfo) -> Unit,
    onNotifyWhenLiveClicked: (ChannelInfo) -> Unit,
    onDeleteClicked: (ChannelInfo) -> Unit,
    onNotifyRangeSettingChanged: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var settingsExpanded by remember {
        mutableStateOf(false)
    }

    LazyColumn(
        content = {
            item {
                TopBar(
                    notificationsEnabled = true,
                    settingsVisible = settingsExpanded,
                    appSettings = appSettings,
                    syncJobEnabled = syncJobRunning,
                    onNotificationsClick = { /*TODO*/ },
                    onSettingsClick = { settingsExpanded = !settingsExpanded },
                    onNotifyRangeChanged = onNotifyRangeSettingChanged,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            items(
                count = channels.size,
                key = { index -> channels[index].id }
            ) {
                SwipeToDismissRow(
                    item = channels[it],
                    onDismissed = onDeleteClicked,
                ) { channelInfo ->
                    ChannelCard(
                        channelInfo = channelInfo,
                        onChannelClicked = onChannelClicked,
                        onNotifyWhenLiveClicked = onNotifyWhenLiveClicked,
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
            }
            item { Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars)) }
        },
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
    )
}