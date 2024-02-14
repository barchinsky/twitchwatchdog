package com.m.twitchwatchdog.dashboard.model

data class DashboardScreenState(
    val channels: List<ChannelInfo> = emptyList()
)