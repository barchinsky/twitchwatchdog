package com.m.twitchwatchdog.dashboard.addChannel

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.m.twitchwatchdog.infrastructure.ui.switchRow.SwitchRow
import com.m.twitchwatchdog.ui.theme.TwitchWatchdogTheme

@Composable
fun AddChannelCard(
    expanded: Boolean,
    onAddChannelClicked: () -> Unit,
    onSaveChannelClicked: (String, Boolean) -> Unit,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val defaultBackgroundColor = Color.Transparent

    var channelName by remember { mutableStateOf("") }
    var shouldNotify by remember { mutableStateOf(false) }
    val isSaveEnabled by remember { derivedStateOf { channelName.isNotBlank() } }

    val animatedBackgroundColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.secondaryContainer.takeIf { expanded }
            ?: defaultBackgroundColor,
        label = "Background color"
    )
    val animatedCardPadding by animateDpAsState(
        targetValue = if (expanded) 8.dp else 0.dp,
        label = "Add channel card padding"
    )

    val animatedAddButtonAlignment by animateHorizontalAlignmentAsState(expanded)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .padding(animatedCardPadding)
            .background(animatedBackgroundColor, RoundedCornerShape(6.dp))
            .imePadding()
            .then(modifier),
    ) {
        Column {
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Icon(Icons.Filled.Close,
                        contentDescription = "Close",
                        modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.End)
                            .clickable {
                                onCloseClicked()
                                channelName = ""
                            }
                    )
                    OutlinedTextField(
                        value = channelName,
                        onValueChange = { channelName = it },
                        label = { Text("Twitch channel") },
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.heightIn(8.dp))
                    SwitchRow(
                        title = "Notify when live",
                        checked = shouldNotify,
                        onCheckedChanged = { shouldNotify = !shouldNotify },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            AddChannelButton(
                expanded = expanded,
                enabled = isSaveEnabled,
                onAddChannelClick = onAddChannelClicked,
                onSaveChannelClick = { onSaveChannelClicked(channelName, shouldNotify) },
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp)
                    .systemBarsPadding()
                    .align(animatedAddButtonAlignment)
            )
        }
    }
}

@Composable
private fun animateHorizontalAlignmentAsState(
    expanded: Boolean,
): State<BiasAlignment.Horizontal> {
    val bias by animateFloatAsState(
        0f.takeIf { expanded } ?: 1f,
        label = "Horizontal bias value",
    )
    return remember { derivedStateOf { BiasAlignment.Horizontal(bias) } }
}

@Composable
@PreviewLightDark
fun AddChannelCardPreview() {
    TwitchWatchdogTheme {
        Surface {
            Box(modifier = Modifier.fillMaxSize()) {
                AddChannelCard(
                    expanded = false,
                    onAddChannelClicked = {},
                    onSaveChannelClicked = { _, _ -> },
                    onCloseClicked = {},
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}