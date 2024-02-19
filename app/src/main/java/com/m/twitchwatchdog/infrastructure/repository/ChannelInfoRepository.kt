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

    suspend fun fetchChannels(): List<ChannelInfo> =
        channelInfoLocalDataSource.getChannels().map { storedChannelInfo ->
            runCatching { channelInfoRemoteDataSource.fetchChannelInfo(storedChannelInfo) }
                .getOrDefault(storedChannelInfo)
        }.also {
            channelInfoLocalDataSource.saveChannels(it)
            channelsFlow.emit(it)
        }

    suspend fun saveChannels(channels: List<ChannelInfo>) {
        channelInfoLocalDataSource.saveChannels(channels)
        channelsFlow.emit(channels)
    }

    suspend fun addChannel(channel: ChannelInfo): List<ChannelInfo> =
        channelInfoLocalDataSource.addChannel(channel)
            .run { fetchChannels() }
}