package com.m.twitchwatchdog.dashboard.usecase

import com.m.twitchwatchdog.dashboard.repository.ChannelInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class FetchChannelInfoUseCase @Inject constructor(
    private val channelInfoRepository: ChannelInfoRepository,
) {

    suspend fun execute() = withContext(Dispatchers.IO) {
        channelInfoRepository.fetchChannels()
    }
}