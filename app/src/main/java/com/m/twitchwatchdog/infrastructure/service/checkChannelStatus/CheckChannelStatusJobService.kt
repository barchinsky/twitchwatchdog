package com.m.twitchwatchdog.infrastructure.service.checkChannelStatus

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.m.twitchwatchdog.MainActivity
import com.m.twitchwatchdog.R
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.infrastructure.useCase.FetchChannelInfoUseCase
import com.m.twitchwatchdog.infrastructure.useCase.ShouldCheckChannelStatusUseCase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class CheckChannelStatusJobService @Inject constructor() : JobService() {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var fetchChannelInfoUseCase: FetchChannelInfoUseCase

    @Inject
    lateinit var shouldCheckChannelStatusUseCase: ShouldCheckChannelStatusUseCase

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onStartJob(params: JobParameters?): Boolean {
        println("Should check status: ${shouldCheckChannelStatusUseCase.execute()}")
        if (!shouldCheckChannelStatusUseCase.execute()) {
            jobFinished(params, false)
            return true
        }

        coroutineScope.launch {
            println("Fetching channel info...")
            runCatching {
                val channelInfo = fetchChannelInfoUseCase.execute()

                val liveChannels =
                    channelInfo.filter { it.notifyWhenLive && it.status == ChannelInfo.Status.LIVE }

                if (liveChannels.isNotEmpty()) {
                    val liveChannelNames = liveChannels.joinToString(",") { it.name }
                    showNotificationIfNeeded(context.getString(R.string.channels_online, liveChannelNames))
                }
            }.onFailure {
                println("Failed to complete fetching job!. $it")
            }

            jobFinished(params, false)
        }

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        coroutineScope.coroutineContext.cancelChildren()
        return false
    }

    private fun showNotificationIfNeeded(content: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Don't notify if previous notification is not cancelled
        if (notificationManager.activeNotifications.any { it.id == content.hashCode() }) {
            return
        }

        val notification = NotificationCompat
            .Builder(
                context,
                context.getString(R.string.default_notification_channel_id)
            )
            .setContentTitle(getString(R.string.channel_is_live_title))
            .setContentText(content)
            .setContentIntent(getContentIntent())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(content.hashCode(), notification)
    }

    private fun getContentIntent(): PendingIntent {
        val mainActivityIntent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        return PendingIntent.getActivity(
            context,
            100,
            mainActivityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}