package com.m.twitchwatchdog.dashboard.ui.expandableChannelCard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.ui.channelCard.ChannelCard
import com.m.twitchwatchdog.dashboard.ui.channelInfo.ChannelInfoCard
import com.m.shared.ui.theme.TwitchWatchdogTheme

@Composable
fun ExpandableChannelCard(
    channelInfo: ChannelInfo,
    onChannelClicked: (ChannelInfo) -> Unit,
    onNotifyWhenLiveClicked: (ChannelInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.elevatedCardColors(),
    ) {
        ChannelCard(channelInfo = channelInfo, onChannelClicked = onChannelClicked)

        AnimatedVisibility(visible = channelInfo.expanded) {
            ChannelInfoCard(
                channelInfo = channelInfo,
                onNotifyWhenLiveClicked = onNotifyWhenLiveClicked,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun ExpandableChannelCardPreview() {
    com.m.shared.ui.theme.TwitchWatchdogTheme {
        Surface {
            ExpandableChannelCard(
                channelInfo = ChannelInfo.getDefault(1, "Channel1", false),
                onChannelClicked = {},
                onNotifyWhenLiveClicked = {}
            )
        }
    }
}