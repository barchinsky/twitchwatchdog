package com.m.twitchwatchdog.dashboard.useCase

import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.infrastructure.repository.ChannelInfoRepository
import javax.inject.Inject

class DeleteChannelUseCase @Inject constructor(
    private val channelInfoRepository: ChannelInfoRepository,
) {

    suspend fun execute(channelInfo: ChannelInfo) =
        channelInfoRepository.deleteChannel(channelInfo)
}