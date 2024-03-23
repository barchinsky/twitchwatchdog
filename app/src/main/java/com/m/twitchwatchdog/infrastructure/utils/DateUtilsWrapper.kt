package com.m.twitchwatchdog.infrastructure.utils

import android.text.format.DateUtils as AndroidDateUtils

internal object DateUtilsWrapper {

    fun isToday(timestamp: Long): Boolean =
        AndroidDateUtils.isToday(timestamp)
}