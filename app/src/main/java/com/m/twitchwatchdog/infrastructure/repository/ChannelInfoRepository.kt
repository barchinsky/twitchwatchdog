package com.m.twitchwatchdog.infrastructure.repository

import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.infrastructure.datasource.ChannelInfoLocalDataSource
import com.m.twitchwatchdog.infrastructure.datasource.ChannelInfoRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelInfoRepository @Inject constructor(
    private val channelInfoRemoteDataSource: ChannelInfoRemoteDataSource,
    private val channelInfoLocalDataSource: ChannelInfoLocalDataSource,
) {

    suspend fun getChannels(): List<ChannelInfo> =
        channelInfoLocalDataSource.getChannels().map { storedChannelInfo ->
            runCatching { channelInfoRemoteDataSource.fetchChannelInfo(storedChannelInfo) }
                .getOrDefault(storedChannelInfo)
        }.also { channelInfoLocalDataSource.saveChannels(it) }

    suspend fun saveChannels(channels: List<ChannelInfo>) {
        channelInfoLocalDataSource.saveChannels(channels)
    }

    suspend fun addChannel(channel: ChannelInfo): List<ChannelInfo> =
        channelInfoLocalDataSource.addChannel(channel)
            .run { getChannels() }
}