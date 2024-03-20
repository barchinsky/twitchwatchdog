package com.m.twitchwatchdog.infrastructure.usecase

import com.m.twitchwatchdog.infrastructure.repository.NotificationRecordRepository
import com.m.twitchwatchdog.infrastructure.utils.DateUtilsWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetNotificationCompliantChannelsUseCase @Inject constructor(
    private val notificationRecordRepository: NotificationRecordRepository,
) {

    suspend fun execute(channelCandidates: List<String>): List<String> =
        withContext(Dispatchers.IO) {
            notificationRecordRepository.get()
                .let {
                    if (DateUtilsWrapper.isToday(it.timestamp)) {
                        channelCandidates.filter { candidate -> candidate !in it.channels }
                    } else {
                        channelCandidates
                    }
                }
        }
}