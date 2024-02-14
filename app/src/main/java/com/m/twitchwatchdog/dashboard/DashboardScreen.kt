package com.m.twitchwatchdog.dashboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.m.twitchwatchdog.dashboard.channelCard.ChannelCard
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.model.DashboardScreenState
import com.m.twitchwatchdog.ui.theme.TwitchWatchdogTheme

@Composable
fun DashboardScreen(
    state: DashboardScreenState,
    onNotifyWhenLiveClicked: (ChannelInfo) -> Unit,
) {
    LazyColumn(
        content = {
            items(state.channels.size) {
                ChannelCard(
                    channelInfo = state.channels[it],
                    onNotifyWhenLiveClicked = onNotifyWhenLiveClicked,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
        },
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.statusBarsPadding().systemBarsPadding()
    )
}

@Composable
@Preview
private fun DashboardScreenPreview() {
    val channels = listOf(
        ChannelInfo(
            id = 1,
            name = "S1mple",
            status = ChannelInfo.Status.LIVE,
            avatarUrl = "https://static-cdn.jtvnw.net/jtv_user_pictures/65232217-c113-40ce-aaa9-50b06a6ee8fa-profile_image-150x150.png",
            expanded = false,
            loading = false
        ),
        ChannelInfo(
            id = 2,
            name = "stewie2k",
            status = ChannelInfo.Status.OFFLINE,
            avatarUrl = "https://static-cdn.jtvnw.net/jtv_user_pictures/00563f70-d05b-4786-addd-5a3b43c2e5b4-profile_image-150x150.png",
            expanded = false,
            loading = false
        ),
    )

    TwitchWatchdogTheme {
        Surface {
            DashboardScreen(
                state = DashboardScreenState(channels),
                onNotifyWhenLiveClicked = {})
        }
    }
}