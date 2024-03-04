package com.m.twitchwatchdog.infrastructure.datasource.settings

import com.m.twitchwatchdog.infrastructure.datasource.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsLocalDataSource {

    val settingsFlow: Flow<AppSettings>

    suspend fun setSettings(settings: AppSettings)
}