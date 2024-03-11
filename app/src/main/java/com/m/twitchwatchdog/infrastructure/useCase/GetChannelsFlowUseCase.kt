package com.m.twitchwatchdog.infrastructure.useCase

import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.repository.ChannelInfoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetChannelsFlowUseCase @Inject constructor(
    private val channelInfoRepository: ChannelInfoRepository,
) {

    fun execute(): Flow<List<ChannelInfo>> =
        channelInfoRepository.getChannelsFlow()
}