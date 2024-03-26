package com.m.twitchwatchdog.dashboard.ui.channelInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m.shared.ui.theme.TwitchWatchdogTheme
import com.m.twitchwatchdog.dashboard.model.ChannelInfo

@Composable
fun ChannelInfoCard(
    channelInfo: ChannelInfo,
    onNotifyWhenLiveClicked: (ChannelInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary)
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (channelInfo.isLive() && channelInfo.streamName?.isNotBlank() == true) {
            Text(
                text = channelInfo.streamName,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Notify when channel is live", color = MaterialTheme.colorScheme.onPrimary)
            Switch(
                checked = channelInfo.notifyWhenLive,
                onCheckedChange = { onNotifyWhenLiveClicked(channelInfo) },
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Watching now", color = MaterialTheme.colorScheme.onPrimary)
            Text(
                text = channelInfo.watchingNow.takeIf { it.isNotBlank() } ?: "-",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@PreviewLightDark
@Composable
fun ChannelInfoPreview() {
    TwitchWatchdogTheme {
        ChannelInfoCard(
            channelInfo = ChannelInfo.getDefault(
                1,
                "PGL",
                true,
            ).copy(
                status = ChannelInfo.Status.LIVE,
                streamName = "Just chatting!"
            ),
            onNotifyWhenLiveClicked = {}
        )
    }
}