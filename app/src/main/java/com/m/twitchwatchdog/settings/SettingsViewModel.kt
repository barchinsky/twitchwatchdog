package com.m.twitchwatchdog.settings

import com.m.twitchwatchdog.settings.datasource.model.AppSettings
import kotlinx.coroutines.flow.StateFlow

interface SettingsViewModel {

    val state: StateFlow<AppSettings>

    fun onNotifyRangeChanged(from: Int, to: Int)
}