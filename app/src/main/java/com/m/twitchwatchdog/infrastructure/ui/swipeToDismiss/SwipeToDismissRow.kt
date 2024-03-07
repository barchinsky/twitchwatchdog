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
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
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
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart) {
                isRemoved = true
                vibrator.vibrate(vibrationEffect)
                true
            } else {
                false
            }
        }
    )

    // Reset swipe to dismiss state in case dismiss has not been finished
    // But composable has been destroyed (due to scroll)
    LaunchedEffect(key1 = isRemoved) {
        if (!isRemoved) {
            coroutineScope.launch {
                dismissState.reset()
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
        SwipeToDismiss(
            state = dismissState,
            background = { SwipeToDismissBackground(state = dismissState) },
            dismissContent = { content(item) },
            directions = setOf(DismissDirection.EndToStart)
        )
    }

    AnimatedVisibility(visible = isRemoved) {
        DismissConfirmationCard(
            dismissTarget = dismissTarget,
            onUndoClicked = {
                isRemoved = false
            }, onDismissConfirmed = { onDismissed(item) })
    }
}