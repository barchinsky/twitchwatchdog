package com.m.twitchwatchdog.dashboard.ui.loadingChannels

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.m.twitchwatchdog.R

@Composable
fun LoadingChannels(
    modifier: Modifier = Modifier,
) {

    val loadingComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading))

    LottieAnimation(
        composition = loadingComposition,
        isPlaying = true,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier
            .size(200.dp)
            .then(modifier)
    )
}