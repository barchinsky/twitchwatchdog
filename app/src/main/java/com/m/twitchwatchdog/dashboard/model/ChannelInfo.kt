package com.m.twitchwatchdog.dashboard.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChannelInfo(
    val id: Long,
    val name: String,
    val status: Status,
    val streamName: String?,
    val avatarUrl: String?,
    val loading: Boolean,
    val expanded: Boolean,
    val notifyWhenLive: Boolean = false,
    val watchingNow: String = "",
) {

    enum class Status {
        LIVE,
        OFFLINE
    }

    fun isLive(): Boolean =
        status == Status.LIVE

    companion object {

        fun getDefault(id: Long, name: String, notifyWhenLive: Boolean = false): ChannelInfo =
            ChannelInfo(
                id = id,
                name = name,
                status = Status.OFFLINE,
                avatarUrl = null,
                loading = false,
                expanded = false,
                notifyWhenLive = notifyWhenLive,
                streamName = null,
            )
    }
}
