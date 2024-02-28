package com.m.twitchwatchdog.infrastructure.datasource

import android.content.SharedPreferences
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelInfoLocalDataSource @Inject constructor(
    moshi: Moshi,
    private val sharedPreferences: SharedPreferences,
) {

    private val readMutex = Mutex()

    private val channelsCache = mutableListOf<ChannelInfo>()

    @OptIn(ExperimentalStdlibApi::class)
    private val channelsInfoAdapter = moshi.adapter<List<ChannelInfo>>()

    suspend fun getChannels(): List<ChannelInfo> = withContext(Dispatchers.IO) {
        readMutex.withLock {
            if (channelsCache.isEmpty()) {
                val channels = sharedPreferences.getString(KEY_CHANNELS, null)?.let {
                    channelsInfoAdapter.fromJson(it)
                } ?: defaultChannels
                channelsCache.addAll(channels)
                channelsCache.toList()
            } else {
                channelsCache.toList()
            }
        }
    }

    suspend fun saveChannels(channelInfo: List<ChannelInfo>): Unit = withContext(Dispatchers.IO) {
        channelsCache.clear()
        channelsCache.addAll(channelInfo)
        dumpChannels()
    }

    suspend fun addChannel(channelInfo: ChannelInfo): List<ChannelInfo> = withContext(Dispatchers.IO) {
        channelsCache
            .apply {
                add(channelInfo)
                dumpChannels()
            }
    }

    suspend fun updateChannel(channelInfo: ChannelInfo): List<ChannelInfo> = withContext(Dispatchers.IO) {
        channelsCache
            .also { channels ->
                val targetChannelIndex = channels.indexOfFirst { it.id == channelInfo.id }

                channels[targetChannelIndex] = channelInfo
                dumpChannels()
            }
    }

    suspend fun deleteChannel(channelInfo: ChannelInfo): List<ChannelInfo> = withContext(Dispatchers.IO) {
        channelsCache
            .filter { it.id != channelInfo.id }
            .also {
                saveChannels(it)
            }
    }

    private suspend fun dumpChannels() = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .putString(KEY_CHANNELS, channelsInfoAdapter.toJson(channelsCache))
            .apply()
    }

    private companion object {

        const val KEY_CHANNELS = "channels_key"

        val defaultChannels = listOf(
            ChannelInfo(
                id = 1,
                name = "rocketleague",
                status = ChannelInfo.Status.OFFLINE,
                avatarUrl = null,
                expanded = false,
                loading = false
            ),
        )
    }
}