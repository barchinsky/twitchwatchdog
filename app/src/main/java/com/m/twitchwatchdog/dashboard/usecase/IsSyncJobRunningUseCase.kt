package com.m.twitchwatchdog.dashboard.usecase

import android.app.job.JobScheduler
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class IsSyncJobRunningUseCase @Inject constructor(
    @ApplicationContext context: Context
) {

    private val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

    fun execute(): Boolean =
        jobScheduler.getPendingJob(EnableChannelAlertUseCase.JOB_ID_ENABLE_ALERT.hashCode()) != null
}