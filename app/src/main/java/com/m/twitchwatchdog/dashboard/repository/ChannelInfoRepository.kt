package com.m.twitchwatchdog.dashboard.repository

import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.datasource.ChannelInfoLocalDataSource
import com.m.twitchwatchdog.dashboard.datasource.ChannelInfoRemoteDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ChannelInfoRepository @Inject constructor(
    private val channelInfoRemoteDataSource: ChannelInfoRemoteDataSource,
    private val channelInfoLocalDataSource: ChannelInfoLocalDataSource,
) {

    private val channelsFlow = MutableSharedFlow<List<ChannelInfo>>(replay = 1)

    fun getChannelsFlow(): Flow<List<ChannelInfo>> =
        channelsFlow

    suspend fun fetchChannels(): Unit = coroutineScope {
        val channels = channelInfoLocalDataSource.getChannels()
            .map { storedChannelInfo ->
                async {
                    runCatching {
                        val remoteChannel =
                            channelInfoRemoteDataSource.fetchChannelInfo(storedChannelInfo)

                        storedChannelInfo.copy(
                            status = remoteChannel.status,
                            avatarUrl = remoteChannel.avatarUrl,
                            watchingNow = remoteChannel.watchingNow,
                            streamName = remoteChannel.streamName,
                        )
                    }.getOrDefault(storedChannelInfo)
                }
            }
            .map { it.await() }
            .sortedBy { it.status }

        set(channels)
    }

    suspend fun set(channels: List<ChannelInfo>) {
        withChannelsFlowUpdate { channelInfoLocalDataSource.saveChannels(channels) }
    }

    suspend fun add(channel: ChannelInfo) =
        withChannelsFlowUpdate { channelInfoLocalDataSource.addChannel(channel) }
            .also { fetchChannels() }

    suspend fun delete(channel: ChannelInfo) =
        withChannelsFlowUpdate { channelInfoLocalDataSource.deleteChannel(channelInfo = channel) }

    suspend fun update(channel: ChannelInfo) {
        withChannelsFlowUpdate { channelInfoLocalDataSource.updateChannel(channel) }
    }

    private suspend fun withChannelsFlowUpdate(block: suspend () -> List<ChannelInfo>) {
        channelsFlow.emit(block())
    }
}