package com.m.twitchwatchdog.dashboard.channelCard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.ui.theme.Green40
import com.m.twitchwatchdog.ui.theme.Orange40
import com.m.twitchwatchdog.ui.theme.TwitchWatchdogTheme

@Composable
fun ChannelCard(
    channelInfo: ChannelInfo,
    onNotifyWhenLiveClicked: (ChannelInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.elevatedCardColors(),
        border = BorderStroke(
            width = 1.dp,
            color = Green40.takeIf { channelInfo.status == ChannelInfo.Status.LIVE } ?: Orange40)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .defaultMinSize(minHeight = 90.dp)
                .then(modifier),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                AsyncImage(
                    model = channelInfo.avatarUrl,
                    contentDescription = "${channelInfo.name}'s avatar",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(50.dp, 50.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(channelInfo.name, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.width(16.dp))
            }
            Switch(checked = channelInfo.notifyWhenLive, onCheckedChange = {
                onNotifyWhenLiveClicked(channelInfo)
            })
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChannelCardPreview() {
    TwitchWatchdogTheme {
        ChannelCard(
            channelInfo = ChannelInfo(
                id = 1,
                "s1mple",
                ChannelInfo.Status.LIVE,
                "",
                loading = false,
                expanded = false,
                notifyWhenLive = false
            ),
            onNotifyWhenLiveClicked = {}
        )
    }
}