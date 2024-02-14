package com.m.twitchwatchdog.dashboard.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChannelInfo(
    val id: Int,
    val name: String,
    val status: Status,
    val avatarUrl: String?,
    val loading: Boolean,
    val expanded: Boolean,
    val notifyWhenLive: Boolean = false,
) {

    enum class Status {
        LIVE,
        OFFLINE
    }
}
