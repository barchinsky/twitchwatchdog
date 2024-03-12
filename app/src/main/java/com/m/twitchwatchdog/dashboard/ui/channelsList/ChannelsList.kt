package com.m.twitchwatchdog.dashboard.ui.channelsList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.ui.expandableChannelCard.ExpandableChannelCard
import com.m.twitchwatchdog.dashboard.ui.topBar.TopBar
import com.m.twitchwatchdog.infrastructure.datasource.model.AppSettings
import com.m.shared.ui.swipeToDismiss.SwipeToDismissRow

private val ListFABInsets = 50.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelsList(
    channels: List<ChannelInfo>,
    appSettings: AppSettings,
    syncJobRunning: Boolean,
    refreshing: Boolean,
    onChannelClicked: (ChannelInfo) -> Unit,
    onNotifyWhenLiveClicked: (ChannelInfo) -> Unit,
    onDeleteClicked: (ChannelInfo) -> Unit,
    onNotifyRangeSettingChanged: (Int, Int) -> Unit,
    onSwipeToRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var settingsExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    val pullToRefreshState = rememberPullToRefreshState()

    if (pullToRefreshState.isRefreshing) {
        onSwipeToRefresh()
    }

    LaunchedEffect(refreshing) {
        if (!refreshing) {
            pullToRefreshState.endRefresh()
        }
    }

    Box(
        modifier = Modifier.nestedScroll(pullToRefreshState.nestedScrollConnection),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            content = {
                item {
                    TopBar(
                        notificationsEnabled = true,
                        settingsVisible = settingsExpanded,
                        appSettings = appSettings,
                        syncJobEnabled = syncJobRunning,
                        onNotificationsClick = { /*TODO*/ },
                        onSettingsClick = { settingsExpanded = !settingsExpanded },
                        onNotifyRangeChanged = onNotifyRangeSettingChanged,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                items(
                    count = channels.size,
                    key = { index -> channels[index].id }
                ) {
                    com.m.shared.ui.swipeToDismiss.SwipeToDismissRow(
                        item = channels[it],
                        dismissTarget = channels[it].name,
                        onDismissed = onDeleteClicked,
                    ) { channelInfo ->
                        ExpandableChannelCard(
                            channelInfo = channelInfo,
                            onChannelClicked = onChannelClicked,
                            onNotifyWhenLiveClicked = onNotifyWhenLiveClicked,
                            modifier = Modifier.padding(vertical = 8.dp),
                        )
                    }
                }
                item {
                    Spacer(
                        Modifier
                            .height(ListFABInsets)
                            .windowInsetsBottomHeight(WindowInsets.systemBars)
                    )
                }
            },
            contentPadding = PaddingValues(16.dp),
            modifier = modifier
        )
        PullToRefreshContainer(state = pullToRefreshState)
    }
}