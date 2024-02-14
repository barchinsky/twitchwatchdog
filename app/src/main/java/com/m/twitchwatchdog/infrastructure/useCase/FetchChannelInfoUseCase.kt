package com.m.twitchwatchdog.infrastructure.useCase

import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.infrastructure.repository.ChannelInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchChannelInfoUseCase @Inject constructor(
    private val channelInfoRepository: ChannelInfoRepository,
) {

    suspend fun execute(): List<ChannelInfo> = withContext(Dispatchers.IO) {
        channelInfoRepository.getChannels()
    }
}