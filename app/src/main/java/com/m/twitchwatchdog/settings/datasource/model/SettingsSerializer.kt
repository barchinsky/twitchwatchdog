package com.m.twitchwatchdog.settings.datasource.model

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
internal class SettingsSerializer @Inject constructor(
    moshi: Moshi,
) : Serializer<AppSettings> {

    @OptIn(ExperimentalStdlibApi::class)
    private val settingsAdapter = moshi.adapter<AppSettings>()

    override val defaultValue = AppSettings.getDefault()

    override suspend fun readFrom(input: InputStream): AppSettings =
        try {
            settingsAdapter.fromJson(input.readBytes().decodeToString()) ?: defaultValue
        } catch (exception: Exception) {
            throw Exception("Cannot read deserialize settings.", exception)
        }

    override suspend fun writeTo(t: AppSettings, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(settingsAdapter.toJson(t).encodeToByteArray())
        }
    }
}