package com.m.twitchwatchdog.settings.datasource

import com.m.twitchwatchdog.settings.datasource.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsLocalDataSource {

    val settingsFlow: Flow<AppSettings>

    suspend fun setSettings(settings: AppSettings)
}