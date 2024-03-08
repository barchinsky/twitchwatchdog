package com.m.twitchwatchdog.infrastructure.ui.swipeToDismiss

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val vibrationEffect = VibrationEffect.createOneShot(100L, 70)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDismissRow(
    item: T,
    dismissTarget: String = "",
    dismissAnimationDuration: Int = 500,
    onDismissed: (T) -> Unit,
    content: @Composable (T) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val vibrator = LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    var isRemoved by remember {
        mutableStateOf(false)
    }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                vibrator.vibrate(vibrationEffect)
            }
            true
        }
    )

    if (isRemoved) {
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                delay(500)
                dismissState.reset()
            }
        }
    }

    // Confirm dismiss for item when it was requested but has not finished
    // And composable has been destroyed (due to scroll)
    DisposableEffect(key1 = Unit) {
        onDispose {
            if (isRemoved) {
                onDismissed(item)
            }
        }
    }

    AnimatedVisibility(
        visible = isRemoved.not(),
        exit = shrinkVertically(
            animationSpec = tween(dismissAnimationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut(),
        enter = expandVertically(
            animationSpec = tween(dismissAnimationDuration),
            expandFrom = Alignment.Top
        ) + fadeIn()
    ) {
        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = { SwipeToDismissBackground(state = dismissState) },
            enableDismissFromStartToEnd = false,
            content = { content(item) }
        )
    }

    AnimatedVisibility(visible = isRemoved) {
        DismissConfirmationCard(
            dismissTarget = dismissTarget,
            onUndoClicked = {
                isRemoved = false
                coroutineScope.launch {
                    dismissState.reset()
                }
            },
            onDismissConfirmed = { onDismissed(item) }
        )
    }
}