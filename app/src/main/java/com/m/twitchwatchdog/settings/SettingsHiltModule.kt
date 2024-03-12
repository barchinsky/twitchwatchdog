package com.m.twitchwatchdog.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.m.twitchwatchdog.infrastructure.datasource.model.AppSettings
import com.m.twitchwatchdog.infrastructure.datasource.model.SettingsSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
internal object SettingsHiltModuleProvider {

    @Provides
    fun providesDataStore(
        @ApplicationContext context: Context,
        serializer: SettingsSerializer,
    ): DataStore<AppSettings> =
        DataStoreFactory.create(
            serializer = serializer,
            produceFile = { File("${context.cacheDir.path}/app_preferences.pb") }
        )
}