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

    @OptIn(ExperimentalStdlibApi::class)
    private val channelsInfoAdapter = moshi.adapter<List<ChannelInfo>>()

    suspend fun getChannels(): List<ChannelInfo> = withContext(Dispatchers.IO) {
        readMutex.withLock {
            sharedPreferences.getString(KEY_CHANNELS, null)?.let {
                channelsInfoAdapter.fromJson(it)
            } ?: defaultChannels
        }
    }

    suspend fun saveChannels(channelInfo: List<ChannelInfo>): Unit = withContext(Dispatchers.IO) {
        sharedPreferences.edit()
            .putString(KEY_CHANNELS, channelsInfoAdapter.toJson(channelInfo))
            .commit()
    }

    suspend fun addChannel(channelInfo: ChannelInfo): List<ChannelInfo> = withContext(Dispatchers.IO) {
        getChannels()
            .toMutableList()
            .also {
                it.add(channelInfo)
                saveChannels(it)
            }
    }

    private companion object {

        const val KEY_CHANNELS = "channels_key"

        val defaultChannels = listOf(
            ChannelInfo(
                id = 1,
                name = "s1mple",
                status = ChannelInfo.Status.OFFLINE,
                avatarUrl = null,
                expanded = false,
                loading = false
            ),
        )
    }
}