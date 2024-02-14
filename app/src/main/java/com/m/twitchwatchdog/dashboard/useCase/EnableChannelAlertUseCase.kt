package com.m.twitchwatchdog.dashboard.useCase

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.m.twitchwatchdog.infrastructure.service.checkChannelStatus.CheckChannelStatusJobService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class EnableChannelAlertUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun execute() {
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        val pendingJob = jobScheduler.getPendingJob(JOB_ID_ENABLE_ALERT.hashCode())

        if (pendingJob != null) {
            return
        }

        val jobInfo = JobInfo
            .Builder(
                JOB_ID_ENABLE_ALERT.hashCode(),
                ComponentName(context, CheckChannelStatusJobService::class.java)
            )
            .setPeriodic(TimeUnit.HOURS.toMillis(1))
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(false)
            .build()

        jobScheduler.schedule(jobInfo)
        println("Channel alert enabled.")
    }

    companion object {
        const val JOB_ID_ENABLE_ALERT = "enable_alert_job_id"
    }
}