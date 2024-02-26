package com.m.twitchwatchdog.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.model.DashboardScreenState
import com.m.twitchwatchdog.dashboard.useCase.AddChannelUseCase
import com.m.twitchwatchdog.dashboard.useCase.DeleteChannelUseCase
import com.m.twitchwatchdog.dashboard.useCase.DisableChannelAlertUseCase
import com.m.twitchwatchdog.dashboard.useCase.EnableChannelAlertUseCase
import com.m.twitchwatchdog.dashboard.useCase.IsSyncJobRunningUseCase
import com.m.twitchwatchdog.dashboard.useCase.StoreChannelsInfoUseCase
import com.m.twitchwatchdog.infrastructure.useCase.FetchChannelInfoUseCase
import com.m.twitchwatchdog.infrastructure.useCase.GetChannelsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
internal class DashboardViewModelImpl @Inject constructor(
    private val fetchChannelInfoUseCase: FetchChannelInfoUseCase,
    private val getChannelsFlowUseCase: GetChannelsFlowUseCase,
    private val storeChannelsInfoUseCase: StoreChannelsInfoUseCase,
    private val enableChannelAlertUseCase: EnableChannelAlertUseCase,
    private val disableChannelAlertUseCase: DisableChannelAlertUseCase,
    private val addChannelUseCase: AddChannelUseCase,
    private val deleteChannelUseCase: DeleteChannelUseCase,
    private val isSyncJobRunningUseCase: IsSyncJobRunningUseCase,
) : DashboardViewModel, ViewModel() {

    override val state = MutableStateFlow(DashboardScreenState())

    init {
        viewModelScope.launch {
            runCatching {
                state.update { it.copy(loading = true) }
                fetchChannelInfoUseCase.execute()
                state.update { it.copy(loading = false) }
            }.onFailure { t ->
                println("Failed to fetch channel info: $t")
                state.update { it.copy(loading = false) }
            }
        }

        viewModelScope.launch {
            getChannelsFlowUseCase.execute()
                .collectLatest { channels ->
                    println("New state arrived")
                    state.update { it.copy(channels = channels, syncJobRunning = isSyncJobRunningUseCase.execute()) }
                }
        }
    }

    override fun onChannelClicked(channelInfo: ChannelInfo) {
        viewModelScope.launch {
            val channels = state.value.channels.toMutableList()
            val targetChannelIndex = channels.indexOfFirst { it.id == channelInfo.id }
            channels[targetChannelIndex] = channelInfo.copy(expanded = !channelInfo.expanded)
            runCatching { storeChannelsInfoUseCase.execute(channels) }
                .onFailure { println("Failed to store channels: $it") }
        }
    }

    override fun onSaveChannelClicked(channelName: String, notifyWhenLive: Boolean) {
        println("New channel: $channelName notifyWhenLive: $notifyWhenLive")
        viewModelScope.launch {
            val channelInfo = ChannelInfo.getDefault(
                Calendar.getInstance().timeInMillis,
                channelName,
                notifyWhenLive
            )
            runCatching {
                addChannelUseCase.execute(channelInfo)

                if (channelInfo.notifyWhenLive) {
                    enableChannelAlertUseCase.execute()
                }
            }.onFailure {
                println("Failed to add channel: $it")
            }
        }
    }

    override fun onNotifyWhenLiveClicked(channelInfo: ChannelInfo) {
        viewModelScope.launch {
            val channels = state.value.channels.toMutableList()
            val targetChannel = channels.indexOfFirst { it.id == channelInfo.id }

            channels[targetChannel] = channelInfo.copy(notifyWhenLive = !channelInfo.notifyWhenLive)

            if (channels.any { it.notifyWhenLive }) {
                enableChannelAlertUseCase.execute()
            } else {
                disableChannelAlertUseCase.execute()
            }

            storeChannelsInfoUseCase.execute(channels)
        }
    }

    override fun onDeleteChannelClicked(channelInfo: ChannelInfo) {
        viewModelScope.launch {
            deleteChannelUseCase.execute(channelInfo)
        }
    }
}