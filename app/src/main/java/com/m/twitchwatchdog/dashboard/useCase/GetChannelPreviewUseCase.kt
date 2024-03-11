package com.m.twitchwatchdog.dashboard.useCase

import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.infrastructure.datasource.ChannelInfoRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

class GetChannelPreviewUseCase @Inject constructor(
    private val channelInfoRemoteDataSource: ChannelInfoRemoteDataSource,
) {

    suspend fun execute(channelName: String) = withContext(Dispatchers.IO) {
        channelInfoRemoteDataSource.fetchChannelInfo(
            ChannelInfo.getDefault(
                Random(10).nextLong(),
                channelName,
                false
            )
        )
    }
}