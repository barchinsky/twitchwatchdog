package com.m.twitchwatchdog.infrastructure.datasource

import androidx.datastore.core.DataStore
import com.m.twitchwatchdog.infrastructure.datasource.model.NotificationRecord
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotificationRecordDataStoreDataSource @Inject constructor(
    private val dataStore: DataStore<NotificationRecord>,
): NotificationRecordLocalDataSource {

    override suspend fun get(): NotificationRecord =
        dataStore.data.first()

    override suspend fun set(notificationRecord: NotificationRecord) =
        dataStore.updateData { notificationRecord }
}