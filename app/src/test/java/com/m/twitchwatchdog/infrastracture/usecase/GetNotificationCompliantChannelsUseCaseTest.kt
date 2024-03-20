package com.m.twitchwatchdog.infrastracture.usecase

import com.google.common.truth.Truth
import com.m.twitchwatchdog.infrastructure.datasource.model.NotificationRecord
import com.m.twitchwatchdog.infrastructure.repository.NotificationRecordRepository
import com.m.twitchwatchdog.infrastructure.usecase.GetNotificationCompliantChannelsUseCase
import com.m.twitchwatchdog.infrastructure.utils.DateUtilsWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

internal class GetNotificationCompliantChannelsUseCaseTest {

    @MockK
    private lateinit var notificationRecordRepositoryMock: NotificationRecordRepository

    @InjectMockKs
    private lateinit var useCase: GetNotificationCompliantChannelsUseCase

    private val channel1 = "channel1"
    private val channel2 = "channel2"
    private val channel3 = "channel3"

    @Before
    fun before() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `Given repository returns record with empty channels Then original channels are returned`() =
        runBlocking {
            val expected = listOf(channel1, channel2)

            // Given
            every { DateUtilsWrapper.isToday(0) } returns false
            coEvery { notificationRecordRepositoryMock.get() } returns NotificationRecord(
                0,
                emptyList()
            )

            // When
            val actual = useCase.execute(expected)

            // Then
            Truth.assertThat(actual)
                .isEqualTo(expected)
        }

    @Test
    fun `Given notificationRecord contains channel1 and timestamp is today Then returns channels without channel1`() =
        runBlocking {
            val expected = listOf(channel2, channel3)

            // Given
            every { DateUtilsWrapper.isToday(1) } returns true
            coEvery { notificationRecordRepositoryMock.get() } returns NotificationRecord(
                1,
                listOf(channel1)
            )

            // When
            val actual = useCase.execute(expected + channel1)

            // Then
            Truth.assertThat(actual)
                .isEqualTo(expected)
        }

    @Test
    fun `Given notificationRecord contains channel1 and timestamp is not today Then returns all channels`() =
        runBlocking {
            val expected = listOf(channel1, channel2, channel3)

            // Given
            every { DateUtilsWrapper.isToday(1) } returns false
            coEvery { notificationRecordRepositoryMock.get() } returns NotificationRecord(
                1,
                listOf(channel1)
            )

            // When
            val actual = useCase.execute(expected)

            // Then
            Truth.assertThat(actual)
                .isEqualTo(expected)
        }

    @Test
    fun `Given notificationRecord contains all candidate channels and timestamp is today Then returns empty list`() =
        runBlocking {
            val input = listOf(channel1, channel2, channel3)

            // Given
            every { DateUtilsWrapper.isToday(1) } returns true
            coEvery { notificationRecordRepositoryMock.get() } returns NotificationRecord(
                1,
                input
            )

            // When
            val actual = useCase.execute(input)

            // Then
            Truth.assertThat(actual)
                .isEmpty()
        }

    companion object {

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            mockkObject(DateUtilsWrapper)
        }

        @AfterClass
        @JvmStatic
        fun afterClass() {
            unmockkAll()
        }
    }
}