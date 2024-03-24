package com.m.twitchwatchdog.dashboard.datasource

import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelInfoRemoteDataSource @Inject constructor() {

    suspend fun fetchChannelInfo(channelInfo: ChannelInfo): ChannelInfo =
        withContext(Dispatchers.IO) {
            val aboutPage = Jsoup.connect("https://m.twitch.tv/${channelInfo.name}/about")
                .get()

            yield()

            val channelLiveTag = aboutPage.getElementsByClass("gALMwg")

            val status = if (channelLiveTag.size > 0) {
                if (channelLiveTag.text().contains("live", ignoreCase = true)) {
                    ChannelInfo.Status.LIVE
                } else {
                    ChannelInfo.Status.OFFLINE
                }
            } else {
                ChannelInfo.Status.OFFLINE
            }

            val watchingNow = if (status == ChannelInfo.Status.LIVE) {
                val twLinkTitle = aboutPage.getElementsByClass("ZyATl").text()
                Regex("""(\d+(\.\d+)?)K?""").find(twLinkTitle)
                    ?.groupValues
                    ?.first()
                    .orEmpty()
            } else ""

            yield()

            val avatarUrl = if (channelInfo.avatarUrl == null) {
                val avatarElements = aboutPage.getElementsByClass("tw-image-avatar")

                if (avatarElements.size > 0) {
                    avatarElements.attr("src")
                } else {
                    null
                }
            } else channelInfo.avatarUrl

            yield()

            val streamName = if (status == ChannelInfo.Status.LIVE) {
                val homePage = Jsoup.connect("https://m.twitch.tv/${channelInfo.name}/home")
                    .get()

                yield()

                homePage.getElementsByClass("bNMEpR")
                    .first()
                    ?.getElementsByTag("h3")
                    ?.attr("title")
            } else null

            channelInfo.copy(
                status = status,
                avatarUrl = avatarUrl,
                watchingNow = watchingNow,
                streamName = streamName
            )
        }
}