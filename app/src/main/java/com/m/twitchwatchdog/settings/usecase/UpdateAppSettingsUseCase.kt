package com.m.twitchwatchdog.settings.usecase

import com.m.twitchwatchdog.infrastructure.datasource.settings.SettingsLocalDataSource
import com.m.twitchwatchdog.infrastructure.datasource.settings.model.AppSettings
import javax.inject.Inject

internal class UpdateAppSettingsUseCase @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource,
) {

    suspend fun execute(newSettings: AppSettings) =
        settingsLocalDataSource.setSettings(newSettings)
}