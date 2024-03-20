package com.m.twitchwatchdog.infrastructure

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.m.twitchwatchdog.infrastructure.datasource.NotificationRecordDataStoreDataSource
import com.m.twitchwatchdog.infrastructure.datasource.NotificationRecordLocalDataSource
import com.m.twitchwatchdog.infrastructure.datasource.model.NotificationRecord
import com.m.twitchwatchdog.infrastructure.datasource.model.NotificationRecordSerializer
import com.m.twitchwatchdog.infrastructure.repository.NotificationRecordRepository
import com.m.twitchwatchdog.infrastructure.repository.NotificationRecordRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
internal object InfrastructureHiltProvider {

    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    fun providesNotificationRecordsDataStore(
        @ApplicationContext context: Context,
        serializer: NotificationRecordSerializer,
    ): DataStore<NotificationRecord> =
        DataStoreFactory.create(
            serializer = serializer,
            produceFile = { File("${context.cacheDir.path}/notification_records.pb") }
        )
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class InfrastructureHiltModuleBinder {

    @Binds
    abstract fun bindsNotificationRecordLocalDataSource(dataSourceImpl: NotificationRecordDataStoreDataSource): NotificationRecordLocalDataSource

    @Binds
    abstract fun bindsNotificationRecordRepository(repo: NotificationRecordRepositoryImpl): NotificationRecordRepository
}