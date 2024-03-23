package com.m.twitchwatchdog.infrastructure.repository

import com.m.twitchwatchdog.infrastructure.datasource.NotificationRecordLocalDataSource
import com.m.twitchwatchdog.infrastructure.datasource.model.NotificationRecord
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotificationRecordRepositoryImpl @Inject constructor(
    private val notificationRecordDataSource: NotificationRecordLocalDataSource,
) : NotificationRecordRepository {

    override suspend fun get(): NotificationRecord =
        notificationRecordDataSource.get()

    override suspend fun set(notificationRecord: NotificationRecord) {
        notificationRecordDataSource.set(notificationRecord)
    }
}