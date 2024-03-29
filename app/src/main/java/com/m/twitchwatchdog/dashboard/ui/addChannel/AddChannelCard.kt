package com.m.twitchwatchdog.dashboard.ui.addChannel

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.m.shared.ui.switchRow.SwitchRow
import com.m.shared.ui.theme.DarkGreyAlpha80
import com.m.shared.ui.theme.TwitchWatchdogTheme
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.ui.channelCard.ChannelCard
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

private val expandedModifier = Modifier.fillMaxSize()
private val PreviewCountdownDelay = TimeUnit.MILLISECONDS.toMillis(1000)
private val PreviewCountdownDuration = TimeUnit.SECONDS.toMillis(2).toInt()
private val PreviewCountdownResetDuration = TimeUnit.MILLISECONDS.toMillis(300).toInt()

@Composable
fun AddChannelCard(
    expanded: Boolean,
    onAddChannelClicked: () -> Unit,
    onSaveChannelClicked: (String, Boolean) -> Unit,
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
    channelPreview: ChannelInfo? = null,
    onRequestPreview: (String) -> Unit = {},
) {
    val defaultBackgroundColor = Color.Transparent
    val softKeyboardController = LocalSoftwareKeyboardController.current

    var channelName by rememberSaveable { mutableStateOf("") }
    var shouldNotify by rememberSaveable { mutableStateOf(false) }
    val isSaveEnabled by remember { derivedStateOf { channelName.isNotBlank() } }

    val animatedBackgroundColor by animateColorAsState(
        targetValue = DarkGreyAlpha80.takeIf { expanded }
            ?: defaultBackgroundColor,
        label = "Background color"
    )
    val animatedCardPadding by animateDpAsState(
        targetValue = if (expanded) 8.dp else 0.dp,
        label = "Add channel card padding"
    )

    val animatedAddButtonAlignment by animateHorizontalAlignmentAsState(expanded)

    var shouldStartPreviewCountdown by remember {
        mutableStateOf(false)
    }
    val previewFetchCountdown by animateFloatAsState(
        targetValue = 0f.takeIf { shouldStartPreviewCountdown } ?: 1f,
        animationSpec = tween(PreviewCountdownDuration.takeIf { shouldStartPreviewCountdown }
                                  ?: PreviewCountdownResetDuration),
        label = "Fetch preview timeout",
        finishedListener = { finalValue ->
            if (finalValue == 0f) {
                onRequestPreview.invoke(channelName)
            }
            shouldStartPreviewCountdown = false
        }
    )

    LaunchedEffect(channelName) {
        shouldStartPreviewCountdown = false
        if (channelName.isEmpty()) {
            onRequestPreview(channelName)
        }

        delay(PreviewCountdownDelay)
        shouldStartPreviewCountdown = channelName.isNotBlank()
    }

    val initModifier = expandedModifier.takeIf { expanded } ?: Modifier

    Box(
        modifier = initModifier
            .background(animatedBackgroundColor)
            .imePadding()
            .then(modifier),
        contentAlignment = Alignment.BottomEnd,
    ) {
        Column(
            modifier = Modifier
                .padding(animatedCardPadding)
                .background(
                    color = MaterialTheme.colorScheme.background.takeIf { expanded }
                        ?: Color.Transparent,
                    shape = RoundedCornerShape(6.dp)
                )
        ) {
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + expandHorizontally(),
                exit = shrinkVertically() + shrinkHorizontally()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.End)
                            .clickable {
                                channelName = ""
                                softKeyboardController?.hide()
                                onCloseClicked()
                            }
                    )
                    AnimatedVisibility(visible = channelPreview != null && channelName.isNotBlank()) {
                        channelPreview?.let {
                            ChannelCard(channelInfo = it, onChannelClicked = {})
                        }
                    }
                    OutlinedTextField(
                        value = channelName,
                        onValueChange = { channelName = it },
                        label = { Text("Channel name") },
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onSaveChannelClicked(channelName, shouldNotify)
                                channelName = ""
                            }
                        ),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    AnimatedVisibility(visible = shouldStartPreviewCountdown) {
                        LinearProgressIndicator(
                            progress = { previewFetchCountdown },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                        )
                    }
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
                onSaveChannelClick = {
                    onSaveChannelClicked(channelName, shouldNotify)
                    channelName = ""
                },
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

@Composable
@PreviewLightDark
fun AddChannelCardExpandedPreview() {
    TwitchWatchdogTheme {
        Surface {
            Box(modifier = Modifier.fillMaxSize()) {
                AddChannelCard(
                    expanded = true,
                    onAddChannelClicked = {},
                    onSaveChannelClicked = { _, _ -> },
                    onCloseClicked = {},
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
fun AddChannelCardChannelInfoPreview() {
    TwitchWatchdogTheme {
        Surface {
            Box(modifier = Modifier.fillMaxSize()) {
                AddChannelCard(
                    expanded = true,
                    channelPreview = ChannelInfo.getDefault(1, "Channel1", false),
                    onAddChannelClicked = {},
                    onSaveChannelClicked = { _, _ -> },
                    onCloseClicked = {},
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
}