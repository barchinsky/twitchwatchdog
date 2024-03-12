package com.m.twitchwatchdog.dashboard.model

data class DashboardScreenState(
    val channels: List<ChannelInfo> = emptyList(),
    val channelPreview: ChannelInfo? = null,
    val loading: Boolean = false,
    val syncJobRunning: Boolean = false,
    val refreshing: Boolean = false,
)