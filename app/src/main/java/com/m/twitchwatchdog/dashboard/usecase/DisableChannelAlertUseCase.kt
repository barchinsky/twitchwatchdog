package com.m.twitchwatchdog.dashboard.usecase

import android.app.job.JobScheduler
import android.content.Context
import com.m.twitchwatchdog.dashboard.usecase.EnableChannelAlertUseCase.Companion.JOB_ID_ENABLE_ALERT
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DisableChannelAlertUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun execute() {
        println("Disable channel alert")
        (context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler)
            .cancel(JOB_ID_ENABLE_ALERT.hashCode())
    }
}