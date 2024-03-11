package com.m.twitchwatchdog.dashboard.usecase

import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.repository.ChannelInfoRepository
import javax.inject.Inject

internal class DeleteChannelUseCase @Inject constructor(
    private val channelInfoRepository: ChannelInfoRepository,
) {

    suspend fun execute(channelInfo: ChannelInfo) =
        channelInfoRepository.delete(channelInfo)
}