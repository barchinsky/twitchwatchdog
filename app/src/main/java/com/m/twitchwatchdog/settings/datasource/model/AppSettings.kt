package com.m.twitchwatchdog.settings.datasource.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppSettings(
    val checkStartHour: Int,
    val checkEndHour: Int,
) {

    companion object {

        fun getDefault(): AppSettings =
            AppSettings(0, 24)
    }
}