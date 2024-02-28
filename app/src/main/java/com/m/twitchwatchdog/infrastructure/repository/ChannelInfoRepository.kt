package com.m.twitchwatchdog.infrastructure.repository

import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.infrastructure.datasource.ChannelInfoLocalDataSource
import com.m.twitchwatchdog.infrastructure.datasource.ChannelInfoRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelInfoRepository @Inject constructor(
    private val channelInfoRemoteDataSource: ChannelInfoRemoteDataSource,
    private val channelInfoLocalDataSource: ChannelInfoLocalDataSource,
) {

    private val channelsFlow = MutableSharedFlow<List<ChannelInfo>>()

    fun getChannelsFlow(): Flow<List<ChannelInfo>> =
        channelsFlow

    suspend fun fetchChannels(): List<ChannelInfo> {
        val channels = channelInfoLocalDataSource.getChannels()
            .map { storedChannelInfo ->
                runCatching {
                    val remoteChannel =
                        channelInfoRemoteDataSource.fetchChannelInfo(storedChannelInfo)
                    storedChannelInfo.copy(
                        status = remoteChannel.status,
                        avatarUrl = remoteChannel.avatarUrl,
                        watchingNow = remoteChannel.watchingNow,
                    )
                }.getOrDefault(storedChannelInfo)
            }

        channelInfoLocalDataSource.saveChannels(channels)
        channelsFlow.emit(channels)
        return channels
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