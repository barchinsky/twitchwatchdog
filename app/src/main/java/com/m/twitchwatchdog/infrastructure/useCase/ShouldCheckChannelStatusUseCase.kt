package com.m.twitchwatchdog.infrastructure.useCase

import java.util.Calendar
import javax.inject.Inject

internal class ShouldCheckChannelStatusUseCase @Inject constructor() {

    fun execute(): Boolean =
        Calendar.getInstance().get(Calendar.HOUR_OF_DAY) in 14..22
}