package com.m.twitchwatchdog.infrastructure.datasource.settings

import androidx.datastore.core.DataStore
import com.m.twitchwatchdog.infrastructure.datasource.settings.model.AppSettings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDataStoreDataSource @Inject constructor(
    private val settingsDataStore: DataStore<AppSettings>,
) : SettingsLocalDataSource {

    override val settingsFlow
        get() = settingsDataStore.data

    override suspend fun setSettings(settings: AppSettings) {
        settingsDataStore.updateData { settings }
    }
}
