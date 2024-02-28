package com.m.twitchwatchdog.ui.swipeToDismiss

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissBackground(
    state: DismissState,
) {

    val color = MaterialTheme.colorScheme.error
        .takeIf { state.dismissDirection == DismissDirection.EndToStart }
        ?: MaterialTheme.colorScheme.background

    val animatedBackgroundColor by animateColorAsState(
        targetValue = color,
        label = "Swipe to dismiss row background color",
        animationSpec = tween(500)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBackgroundColor, RoundedCornerShape(6.dp))
            .padding(8.dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        Icon(imageVector = Icons.Filled.Delete, contentDescription = null, tint = Color.White)
    }
}