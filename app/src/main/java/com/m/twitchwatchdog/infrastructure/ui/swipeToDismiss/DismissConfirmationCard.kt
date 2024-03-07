package com.m.twitchwatchdog.infrastructure.ui.swipeToDismiss

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.m.twitchwatchdog.ui.theme.TwitchWatchdogTheme
import java.util.concurrent.TimeUnit

@Composable
fun DismissConfirmationCard(
    dismissTarget: String,
    onUndoClicked: () -> Unit,
    onDismissConfirmed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var shouldDismiss by remember {
        mutableStateOf(false)
    }

    val animationDuration = 5L.takeIf { shouldDismiss } ?: 1L

    val undoCountdown by animateFloatAsState(
        targetValue = 0f.takeIf { shouldDismiss } ?: 1f,
        label = "Undo countdown",
        animationSpec = tween(TimeUnit.SECONDS.toMillis(animationDuration).toInt()),
        finishedListener = {
            if (it == 1f) {
                onUndoClicked()
            } else {
                onDismissConfirmed()
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        shouldDismiss = true
    }

    Column(
        modifier = Modifier.then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress = undoCountdown,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Deleting $dismissTarget...")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { shouldDismiss = false }) {
            Text("Cancel")
        }
    }
}

@Preview
@Composable
private fun DismissConfirmationCardPreview() {
    TwitchWatchdogTheme {
        Surface {
            DismissConfirmationCard(
                dismissTarget = "channel",
                onUndoClicked = { /*TODO*/ },
                onDismissConfirmed = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}