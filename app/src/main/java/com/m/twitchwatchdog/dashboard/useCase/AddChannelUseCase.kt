package com.m.twitchwatchdog.dashboard.useCase

import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.infrastructure.repository.ChannelInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AddChannelUseCase @Inject constructor(
    private val channelInfoRepository: ChannelInfoRepository,
) {

    suspend fun execute(channel: ChannelInfo): List<ChannelInfo> = withContext(Dispatchers.IO) {
        channelInfoRepository.addChannel(channel)
    }
}