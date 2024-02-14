package com.m.twitchwatchdog.dashboard

import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.model.DashboardScreenState
import kotlinx.coroutines.flow.StateFlow

interface DashboardViewModel {

    val state: StateFlow<DashboardScreenState>

    fun onChannelClicked(channelInfo: ChannelInfo)

    fun onAddChannelClicked()

    fun onNotifyWhenLiveClicked(channelInfo: ChannelInfo)
}