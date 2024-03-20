package com.m.twitchwatchdog.infrastructure.datasource

import com.m.twitchwatchdog.infrastructure.datasource.model.NotificationRecord

internal interface NotificationRecordLocalDataSource {

    suspend fun get(): NotificationRecord

    suspend fun set(notificationRecord: NotificationRecord): NotificationRecord
}