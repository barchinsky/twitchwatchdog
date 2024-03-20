package com.m.twitchwatchdog.infrastructure

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object InfrastructureHiltProvider {

    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class InfrastructureHiltModuleBinder {

    @Binds
    abstract fun bindsSettingsLocalDataSource(dataSourceImpl: SettingsDataStoreDataSource): SettingsLocalDataSource
}