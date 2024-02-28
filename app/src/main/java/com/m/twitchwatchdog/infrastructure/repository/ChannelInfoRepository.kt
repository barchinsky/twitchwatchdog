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
                        avatarUrl = remoteChannel.avatarUrl
                    )
                }.getOrDefault(storedChannelInfo)
            }

        channelInfoLocalDataSource.saveChannels(channels)
        channelsFlow.emit(channels)
        return channels
    }

    suspend fun set(channels: List<ChannelInfo>) {
        channelInfoLocalDataSource.saveChannels(channels)
        channelsFlow.emit(channels)
    }

    suspend fun add(channel: ChannelInfo) =
        channelInfoLocalDataSource.addChannel(channel)
            .run { fetchChannels() }

    suspend fun delete(channel: ChannelInfo) =
        channelInfoLocalDataSource.deleteChannel(channelInfo = channel)
            .also {
                channelsFlow.emit(it)
            }

    suspend fun update(channel: ChannelInfo) {
        channelInfoLocalDataSource.updateChannel(channel)
        fetchChannels()
    }
}