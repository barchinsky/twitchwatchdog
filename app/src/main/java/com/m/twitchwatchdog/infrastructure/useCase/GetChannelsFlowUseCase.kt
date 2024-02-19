package com.m.twitchwatchdog.infrastructure.useCase

import androidx.lifecycle.Lifecycle
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.infrastructure.repository.ChannelInfoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetChannelsFlowUseCase @Inject constructor(
    private val channelInfoRepository: ChannelInfoRepository,
) {

    fun execute(): Flow<List<ChannelInfo>> =
        channelInfoRepository.getChannelsFlow()
}