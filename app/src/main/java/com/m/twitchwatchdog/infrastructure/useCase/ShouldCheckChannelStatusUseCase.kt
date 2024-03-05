package com.m.twitchwatchdog.infrastructure.useCase

import com.m.twitchwatchdog.infrastructure.datasource.settings.SettingsLocalDataSource
import kotlinx.coroutines.flow.first
import java.util.Calendar
import javax.inject.Inject

internal class ShouldCheckChannelStatusUseCase @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource,
) {

    suspend fun execute(): Boolean {
        val (startHour, endHour) = settingsLocalDataSource.settingsFlow.first()

        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY) in startHour..< endHour
    }

}