package com.m.twitchwatchdog.infrastructure.datasource

import com.m.twitchwatchdog.infrastructure.datasource.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsLocalDataSource {

    val settingsFlow: Flow<AppSettings>

    suspend fun setSettings(settings: AppSettings)
}