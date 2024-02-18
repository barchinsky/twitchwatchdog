package com.m.twitchwatchdog.dashboard.liveChannelBadge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.m.twitchwatchdog.R
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.ui.theme.TwitchWatchdogTheme

@Composable
fun ChannelStatusBadge(
    channelStatus: ChannelInfo.Status,
    modifier: Modifier = Modifier,
) {
    val isLive by remember {
        mutableStateOf(channelStatus == ChannelInfo.Status.LIVE)
    }

    val backgroundColor by remember {
        derivedStateOf { Color.Red.takeIf { isLive } ?: Color.Gray }
    }

    Row(
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(6.dp))
            .padding(vertical = 4.dp, horizontal = 6.dp)
            .then(modifier)
    ) {
        Text(
            text = stringResource(R.string.channel_live.takeIf { isLive }
                ?: R.string.channel_offline),
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
@PreviewLightDark
fun LiveChannelBadgePreview() {
    TwitchWatchdogTheme {
        ChannelStatusBadge(
            channelStatus = ChannelInfo.Status.OFFLINE
        )
    }
}