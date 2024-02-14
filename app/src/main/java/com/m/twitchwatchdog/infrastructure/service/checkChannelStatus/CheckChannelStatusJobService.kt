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
        if (!shouldCheckChannelStatusUseCase.execute()) return true

        coroutineScope.launch {
            println("Fetching channel info...")
            runCatching {
                val channelInfo = fetchChannelInfoUseCase.execute()

                val liveChannels =
                    channelInfo.filter { it.notifyWhenLive && it.status == ChannelInfo.Status.LIVE }

                if (liveChannels.isNotEmpty()) {
                    val liveChannelNames = liveChannels.joinToString(",") { it.name }
                    showNotification(context.getString(R.string.channels_online, liveChannelNames))
                }
            }.onFailure {
                println("Failed to complete fetching job!. $it")
            }
        }

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        coroutineScope.coroutineContext.cancelChildren()
        return true
    }

    private fun showNotification(content: String) {
        val notification = NotificationCompat.Builder(
            context,
            context.getString(R.string.default_notification_channel_id)
        )
            .setContentText(content)
            .setContentIntent(getContentIntent())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
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