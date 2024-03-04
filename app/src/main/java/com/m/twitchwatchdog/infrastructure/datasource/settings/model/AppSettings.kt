package com.m.twitchwatchdog.infrastructure.datasource.settings.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppSettings(
    val checkStartHour: Int,
    val checkEndHour: Int,
)