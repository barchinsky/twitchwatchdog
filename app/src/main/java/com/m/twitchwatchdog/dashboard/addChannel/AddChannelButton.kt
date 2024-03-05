package com.m.twitchwatchdog.dashboard.addChannel

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.m.twitchwatchdog.R
import com.m.twitchwatchdog.ui.theme.TwitchWatchdogTheme

@Composable
fun AddChannelButton(
    expanded: Boolean,
    enabled: Boolean,
    onAddChannelClick: () -> Unit,
    onSaveChannelClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val buttonBackgroundColor = MaterialTheme.colorScheme.primary
        .takeIf { enabled || !expanded }
        ?: MaterialTheme.colorScheme.inversePrimary

    Card(
        shape = CardDefaults.elevatedShape,
        colors = CardDefaults.elevatedCardColors(),
        elevation = CardDefaults.elevatedCardElevation(),
        modifier = Modifier.then(modifier)
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    if (enabled && expanded) {
                        onSaveChannelClick()
                    } else {
                        onAddChannelClick()
                    }
                }
                .background(buttonBackgroundColor, RoundedCornerShape(6.dp))
                .padding(12.dp)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Crossfade(targetState = expanded, label = "Icon") { expanded ->
                Icon(
                    imageVector = Icons.Filled.Done.takeIf { expanded } ?: Icons.Filled.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.save.takeIf { expanded } ?: R.string.add_channel),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun AddChannelButtonPreview() {
    TwitchWatchdogTheme {
        AddChannelButton(
            expanded = false,
            enabled = true,
            onAddChannelClick = {},
            onSaveChannelClick = {}
        )
    }
}