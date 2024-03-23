package com.m.twitchwatchdog.infrastructure.datasource.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class NotificationRecord(
    val timestamp: Long,
    val channels: List<String>,
)
