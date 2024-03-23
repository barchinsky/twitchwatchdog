package com.m.twitchwatchdog.infrastructure.usecase

import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.infrastructure.repository.NotificationRecordRepository
import com.m.twitchwatchdog.infrastructure.utils.DateUtilsWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetNotificationCompliantChannelsUseCase @Inject constructor(
    private val notificationRecordRepository: NotificationRecordRepository,
) {

    suspend fun execute(channelCandidates: List<ChannelInfo>): List<ChannelInfo> =
        withContext(Dispatchers.IO) {
            notificationRecordRepository.get()
                .let {
                    val isRecordForToday = DateUtilsWrapper.isToday(it.timestamp)
                    channelCandidates
                        .filter { candidate ->
                            candidate.status == ChannelInfo.Status.LIVE && candidate.notifyWhenLive
                        }
                        .filter { candidate -> !isRecordForToday || candidate.name !in it.channels }
                }
        }
}