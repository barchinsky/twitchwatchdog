package com.m.twitchwatchdog.infrastructure.datasource

import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelInfoRemoteDataSource @Inject constructor() {

    suspend fun fetchChannelInfo(channelInfo: ChannelInfo): ChannelInfo {
        val page = Jsoup.connect("https://m.twitch.tv/${channelInfo.name}/about")
            .get()

        val channelLiveTag = page.getElementsByClass("gALMwg")

        val status = if (channelLiveTag.size > 0) {
            if (channelLiveTag.text().contains("live", ignoreCase = true)) {
                ChannelInfo.Status.LIVE
            } else {
                ChannelInfo.Status.OFFLINE
            }
        } else {
            ChannelInfo.Status.OFFLINE
        }

        val avatarUrl = if (channelInfo.avatarUrl == null) {
            val avatarElements = page.getElementsByClass("tw-image-avatar")

            if (avatarElements.size > 0) {
                avatarElements.attr("src")
            } else {
                null
            }
        } else channelInfo.avatarUrl

        return channelInfo.copy(status = status, avatarUrl = avatarUrl)
    }
}