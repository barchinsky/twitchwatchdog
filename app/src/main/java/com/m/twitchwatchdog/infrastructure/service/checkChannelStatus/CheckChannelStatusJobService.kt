package com.m.twitchwatchdog.infrastructure.service.checkChannelStatus

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.core.app.NotificationCompat
import com.m.twitchwatchdog.MainActivity
import com.m.twitchwatchdog.R
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.usecase.FetchChannelInfoUseCase
import com.m.twitchwatchdog.dashboard.usecase.GetChannelsFlowUseCase
import com.m.twitchwatchdog.infrastructure.usecase.GetNotificationCompliantChannelsUseCase
import com.m.twitchwatchdog.infrastructure.usecase.ShouldCheckChannelStatusUseCase
import com.m.twitchwatchdog.infrastructure.usecase.AddChannelsToNotificationRecordUseCase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    lateinit var getChannelsFlowUseCase: GetChannelsFlowUseCase

    @Inject
    lateinit var shouldCheckChannelStatusUseCase: ShouldCheckChannelStatusUseCase

    @Inject
    lateinit var addChannelsToNotificationRecordUseCase: AddChannelsToNotificationRecordUseCase

    @Inject
    lateinit var getNotificationCompliantChannelsUseCase: GetNotificationCompliantChannelsUseCase

    private val notificationManager
        get() = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onStartJob(params: JobParameters?): Boolean {
        coroutineScope.launch {
            println("AAA: Fetching channel info...")
            if (shouldCheckChannelStatusUseCase.execute()) {
                runCatching {
                    fetchChannelInfoUseCase.execute()

                    val channels = getChannelsFlowUseCase.execute()
                        .firstOrNull()
                        .orEmpty()

                    if (channels.isNotEmpty()) {
                        showNotificationIfNeeded(channels)
                    } else {
                        addChannelsToNotificationRecordUseCase.execute(emptyList())
                    }
                }.onFailure { println("Failed to complete fetching job!. $it") }

                jobFinished(params, false)
            } else {
                jobFinished(params, false)
            }
        }

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        coroutineScope.coroutineContext.cancelChildren()
        return false
    }

    private suspend fun showNotificationIfNeeded(liveChannels: List<ChannelInfo>) {
        val notificationCompliantChannels =
            getNotificationCompliantChannelsUseCase.execute(liveChannels)

        if (notificationCompliantChannels.isEmpty()) return

        val liveChannelNames = notificationCompliantChannels.joinToString(",") { it.name }
        val content = context.getString(
            R.string.channels_online,
            liveChannelNames
        )
        val notificationId = liveChannelNames.hashCode()

        // Don't notify if previous notification is not cancelled
        if (notificationManager.activeNotifications.any { it.id == notificationId }) {
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
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setLargeIcon(Icon.createWithResource(context, R.drawable.splash_background))
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
        addChannelsToNotificationRecordUseCase.execute(notificationCompliantChannels.map { it.name })
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