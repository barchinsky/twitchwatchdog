package com.m.twitchwatchdog.dashboard.ui.channelCard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.m.shared.ui.theme.TwitchWatchdogTheme
import com.m.twitchwatchdog.R
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.ui.liveChannelBadge.ChannelStatusBadge

@Composable
fun ChannelCard(
    channelInfo: ChannelInfo,
    onChannelClicked: (ChannelInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onChannelClicked(channelInfo) }
            .padding(horizontal = 16.dp)
            .defaultMinSize(minHeight = 90.dp)
            .then(modifier),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(channelInfo.avatarUrl)
                    .placeholder(R.drawable.ic_launcher)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.channel_avatar, channelInfo.name),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(50.dp, 50.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                channelInfo.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Image(
                painter = painterResource(
                    id = R.drawable.ic_notifications_active_24
                        .takeIf { channelInfo.notifyWhenLive }
                        ?: R.drawable.ic_notifications_off_24
                ),
                contentDescription = "Notifications enabled",
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
        ChannelStatusBadge(channelInfo)
    }
}

@Composable
@PreviewLightDark
fun ChannelCardPreview() {
    TwitchWatchdogTheme {
        Surface {
            ChannelCard(
                channelInfo = ChannelInfo(
                    id = 1,
                    "Channel1",
                    ChannelInfo.Status.LIVE,
                    "",
                    watchingNow = "32K",
                    loading = false,
                    expanded = true,
                    notifyWhenLive = true
                ),
                onChannelClicked = {},
            )
        }
    }
}