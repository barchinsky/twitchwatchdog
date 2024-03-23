package com.m.twitchwatchdog.settings.usecase

import com.m.twitchwatchdog.settings.datasource.SettingsLocalDataSource
import com.m.twitchwatchdog.settings.datasource.model.AppSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetSettingsFlowUseCase @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource,
){

    fun execute(): Flow<AppSettings> =
        settingsLocalDataSource.settingsFlow
}