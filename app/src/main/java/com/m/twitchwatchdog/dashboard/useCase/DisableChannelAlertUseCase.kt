package com.m.twitchwatchdog.dashboard.useCase

import android.app.job.JobScheduler
import android.content.Context
import com.m.twitchwatchdog.dashboard.useCase.EnableChannelAlertUseCase.Companion.JOB_ID_ENABLE_ALERT
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DisableChannelAlertUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun execute() {
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

        val pendingJob = jobScheduler.cancel(JOB_ID_ENABLE_ALERT.hashCode())
    }
}