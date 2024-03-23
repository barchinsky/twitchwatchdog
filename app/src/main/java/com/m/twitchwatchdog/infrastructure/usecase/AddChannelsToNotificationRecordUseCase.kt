package com.m.twitchwatchdog.infrastructure.usecase

import com.m.twitchwatchdog.infrastructure.repository.NotificationRecordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

internal class AddChannelsToNotificationRecordUseCase @Inject constructor(
    private val notificationRecordRepository: NotificationRecordRepository,
) {

    suspend fun execute(channels: List<String>) =
        withContext(Dispatchers.IO) {
            val currentRecord = notificationRecordRepository.get()
            val newChannels = (currentRecord.channels + channels).toSet().toList()
            notificationRecordRepository.set(
                currentRecord.copy(
                    timestamp = Calendar.getInstance().timeInMillis,
                    channels = newChannels
                )
            )
        }
}