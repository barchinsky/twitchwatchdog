package com.m.twitchwatchdog.infrastructure.datasource.settings.model

import androidx.datastore.core.Serializer
import com.squareup.moshi.Moshi

import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton
import com.squareup.moshi.adapter

@Singleton
class SettingsSerializer @Inject constructor(
    moshi: Moshi,
) : Serializer<AppSettings> {

    @OptIn(ExperimentalStdlibApi::class)
    private val settingsAdapter = moshi.adapter<AppSettings>()

    override val defaultValue = AppSettings(0, 23)

    override suspend fun readFrom(input: InputStream): AppSettings =
        try {
            settingsAdapter.fromJson(input.readBytes().decodeToString()) ?: defaultValue
        } catch (exception: Exception) {
            throw Exception("Cannot read deserialize settings.", exception)
        }

    override suspend fun writeTo(t: AppSettings, output: OutputStream) {
        output.write(settingsAdapter.toJson(t).encodeToByteArray())
    }
}