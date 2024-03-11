package com.m.twitchwatchdog.infrastracture.repository

import com.google.common.truth.Truth
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.datasource.ChannelInfoLocalDataSource
import com.m.twitchwatchdog.dashboard.datasource.ChannelInfoRemoteDataSource
import com.m.twitchwatchdog.dashboard.repository.ChannelInfoRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ChannelInfoRepositoryTest {

    @RelaxedMockK
    private lateinit var channelInfoLocalDataSourceMock: ChannelInfoLocalDataSource

    @MockK
    private lateinit var channelInfoRemoteDataSourceMock: ChannelInfoRemoteDataSource

    @InjectMockKs
    private lateinit var repository: ChannelInfoRepository

    private val channelMock1 = ChannelInfo.getDefault(1, "1", false)

    @Before
    fun before() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `Given localDataSource is not empty and remoteDataSource returns value When fetchChannels is invoked Then channelsFlow emits channels`() =
        runTest {
            val remoteChannelMock1 = channelMock1.copy(status = ChannelInfo.Status.LIVE)
            val expected = listOf(remoteChannelMock1)

            // Given
            coEvery { channelInfoLocalDataSourceMock.getChannels() } returns listOf(channelMock1)
            coEvery { channelInfoLocalDataSourceMock.saveChannels(expected) } returns expected

            coEvery { channelInfoRemoteDataSourceMock.fetchChannelInfo(channelMock1) } returns remoteChannelMock1

            // When
            repository.fetchChannels()

            // Then
            Truth.assertThat(repository.getChannelsFlow().first())
                .isEqualTo(expected)
        }
}