package com.m.twitchwatchdog.dashboard

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.m.twitchwatchdog.R
import com.m.twitchwatchdog.dashboard.addChannel.AddChannelCard
import com.m.twitchwatchdog.dashboard.channelCard.ChannelCard
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.model.DashboardScreenState
import com.m.twitchwatchdog.ui.theme.TwitchWatchdogTheme

@Composable
fun DashboardScreen(
    state: DashboardScreenState,
    onChannelClicked: (ChannelInfo) -> Unit,
    onNotifyWhenLiveClicked: (ChannelInfo) -> Unit,
    onSaveChannelClicked: (String, Boolean) -> Unit,
) {
    val loadingComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading))
    var isAddChannelExpanded by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .systemBarsPadding()
    ) {
        Crossfade(
            targetState = state.loading,
            label = "Loading content",
            modifier = Modifier.align(Alignment.Center)
        ) { loading ->
            if (loading) {
                LottieAnimation(
                    composition = loadingComposition,
                    isPlaying = true,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(200.dp)
                )
            } else {
                LazyColumn(
                    content = {
                        items(state.channels.size) {
                            ChannelCard(
                                channelInfo = state.channels[it],
                                onChannelClicked = onChannelClicked,
                                onNotifyWhenLiveClicked = onNotifyWhenLiveClicked,
                                modifier = Modifier.padding(vertical = 8.dp),
                            )
                        }
                    },
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        AddChannelCard(
            expanded = isAddChannelExpanded,
            onAddChannelClicked = { isAddChannelExpanded = true },
            onSaveChannelClicked = onSaveChannelClicked,
            onCloseClicked = { isAddChannelExpanded = false },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        )
    }
}

@Composable
@Preview(showSystemUi = true)
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
                state = DashboardScreenState(channels, loading = true),
                onChannelClicked = {},
                onNotifyWhenLiveClicked = {},
                onSaveChannelClicked = { _, _ -> }
            )
        }
    }
}