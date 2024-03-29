package com.m.twitchwatchdog.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.model.DashboardScreenState
import com.m.twitchwatchdog.dashboard.ui.addChannel.AddChannelCard
import com.m.twitchwatchdog.dashboard.ui.channelsList.ChannelsList
import com.m.twitchwatchdog.dashboard.ui.loadingChannels.LoadingChannels
import com.m.twitchwatchdog.dashboard.ui.noChannelsCard.NoChannelsCard
import com.m.twitchwatchdog.settings.datasource.model.AppSettings
import com.m.shared.ui.theme.TwitchWatchdogTheme

@Composable
internal fun DashboardScreen(
    state: DashboardScreenState,
    appSettings: AppSettings,
    onChannelClicked: (ChannelInfo) -> Unit,
    onNotifyWhenLiveClicked: (ChannelInfo) -> Unit,
    onSaveChannelClicked: (String, Boolean) -> Unit,
    onDeleteClicked: (ChannelInfo) -> Unit,
    onNotifyRangeSettingChanged: (Int, Int) -> Unit,
    onSwipeToRefresh: () -> Unit,
    onRequestChannelPreview: (String) -> Unit,
) {

    var isAddChannelExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        when {
            state.loading -> LoadingChannels(modifier = Modifier.align(Alignment.Center))

            state.channels.isEmpty() -> NoChannelsCard(modifier = Modifier.align(Alignment.Center))

            else -> {
                ChannelsList(
                    channels = state.channels,
                    appSettings = appSettings,
                    syncJobRunning = state.syncJobRunning,
                    refreshing = state.refreshing,
                    onChannelClicked = onChannelClicked,
                    onNotifyWhenLiveClicked = onNotifyWhenLiveClicked,
                    onDeleteClicked = onDeleteClicked,
                    onNotifyRangeSettingChanged = onNotifyRangeSettingChanged,
                    onSwipeToRefresh = onSwipeToRefresh,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        AddChannelCard(
            expanded = isAddChannelExpanded,
            channelPreview = state.channelPreview,
            onAddChannelClicked = { isAddChannelExpanded = true },
            onSaveChannelClicked = onSaveChannelClicked,
            onCloseClicked = { isAddChannelExpanded = false },
            modifier = Modifier.align(Alignment.BottomEnd),
            onRequestPreview = onRequestChannelPreview,
        )
    }
}

@Composable
@PreviewLightDark
private fun DashboardScreenPreview() {
    val channels = listOf(
        ChannelInfo(
            id = 1,
            name = "S1mple",
            status = ChannelInfo.Status.LIVE,
            avatarUrl = "https://static-cdn.jtvnw.net/jtv_user_pictures/65232217-c113-40ce-aaa9-50b06a6ee8fa-profile_image-150x150.png",
            expanded = false,
            loading = false,
            streamName = null,
        ),
        ChannelInfo(
            id = 2,
            name = "stewie2k",
            status = ChannelInfo.Status.OFFLINE,
            avatarUrl = "https://static-cdn.jtvnw.net/jtv_user_pictures/00563f70-d05b-4786-addd-5a3b43c2e5b4-profile_image-150x150.png",
            expanded = false,
            loading = false,
            streamName = null,
        ),
    )

    TwitchWatchdogTheme {
        Surface {
            DashboardScreen(
                state = DashboardScreenState(channels, loading = true),
                appSettings = AppSettings.getDefault(),
                onChannelClicked = {},
                onNotifyWhenLiveClicked = {},
                onSaveChannelClicked = { _, _ -> },
                onDeleteClicked = {},
                onNotifyRangeSettingChanged = { _, _ -> },
                onSwipeToRefresh = {},
                onRequestChannelPreview = {}
            )
        }
    }
}