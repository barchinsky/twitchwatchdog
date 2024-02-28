package com.m.twitchwatchdog.dashboard.noChannelsCard

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.m.twitchwatchdog.R
import com.m.twitchwatchdog.ui.theme.TwitchWatchdogTheme

@Composable
fun NoChannelsCard(
    modifier: Modifier = Modifier,
) {
    val noChannelsAnimation by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_no_channels))

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(composition = noChannelsAnimation, isPlaying = true)
        Text(
            text = "No channels added yet!\n Add the first one!",
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun NoChannelsCardPreview() {
    TwitchWatchdogTheme {
        Surface {
            NoChannelsCard()
        }
    }
}