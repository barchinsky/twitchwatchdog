package com.m.twitchwatchdog.infrastructure.repository

import com.m.twitchwatchdog.infrastructure.datasource.model.NotificationRecord

internal interface NotificationRecordRepository {

    suspend fun get(): NotificationRecord

    suspend fun set(notificationRecord: NotificationRecord)
}