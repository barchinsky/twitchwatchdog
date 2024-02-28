package com.m.twitchwatchdog.dashboard.useCase

import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.infrastructure.repository.ChannelInfoRepository
import javax.inject.Inject

class UpdateChannelUseCase @Inject constructor(
    private val channelInfoRepository: ChannelInfoRepository
) {

    suspend fun execute(channel: ChannelInfo) =
        channelInfoRepository.update(channel)

}