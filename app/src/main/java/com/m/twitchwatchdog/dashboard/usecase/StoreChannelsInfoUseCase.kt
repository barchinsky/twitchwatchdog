package com.m.twitchwatchdog.dashboard.usecase

import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.repository.ChannelInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class StoreChannelsInfoUseCase @Inject constructor(
    private val channelInfoRepository: ChannelInfoRepository,
) {

    suspend fun execute(channels: List<ChannelInfo>) {
        withContext(Dispatchers.IO) {
            channelInfoRepository.set(channels)
        }
    }
}