package com.m.twitchwatchdog.infrastructure.datasource.model

import androidx.datastore.core.Serializer
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NotificationRecordSerializer @Inject constructor(
    moshi: Moshi,
) : Serializer<NotificationRecord> {

    @OptIn(ExperimentalStdlibApi::class)
    private val notificationRecordAdapter = moshi.adapter<NotificationRecord>()

    override val defaultValue = NotificationRecord(0, emptyList())

    override suspend fun readFrom(input: InputStream): NotificationRecord =
        try {
            notificationRecordAdapter.fromJson(input.readBytes().decodeToString()) ?: defaultValue
        } catch (exception: Exception) {
            throw Exception("Cannot read deserialize settings.", exception)
        }

    override suspend fun writeTo(t: NotificationRecord, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(notificationRecordAdapter.toJson(t).encodeToByteArray())
        }
    }
}