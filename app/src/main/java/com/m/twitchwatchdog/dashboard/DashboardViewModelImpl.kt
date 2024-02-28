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
import com.m.twitchwatchdog.dashboard.useCase.UpdateChannelUseCase
import com.m.twitchwatchdog.infrastructure.useCase.FetchChannelInfoUseCase
import com.m.twitchwatchdog.infrastructure.useCase.GetChannelsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
internal class DashboardViewModelImpl @Inject constructor(
    getChannelsFlowUseCase: GetChannelsFlowUseCase,
    private val fetchChannelInfoUseCase: FetchChannelInfoUseCase,
    private val enableChannelAlertUseCase: EnableChannelAlertUseCase,
    private val disableChannelAlertUseCase: DisableChannelAlertUseCase,
    private val addChannelUseCase: AddChannelUseCase,
    private val deleteChannelUseCase: DeleteChannelUseCase,
    private val updateChannelUseCase: UpdateChannelUseCase,
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

        getChannelsFlowUseCase.execute()
            .onEach { channels ->
                if (channels.any { it.notifyWhenLive }) {
                    enableChannelAlertUseCase.execute()
                } else {
                    disableChannelAlertUseCase.execute()
                }

                state.update {
                    it.copy(channels = channels, syncJobRunning = isSyncJobRunningUseCase.execute())
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onChannelClicked(channelInfo: ChannelInfo) {
        viewModelScope.launch {
            runCatching { updateChannelUseCase.execute(channelInfo.copy(expanded = !channelInfo.expanded)) }
                .onFailure { println("Failed to update channel: $it") }
        }
    }

    override fun onSaveChannelClicked(channelName: String, notifyWhenLive: Boolean) {
        viewModelScope.launch {
            val channelInfo = ChannelInfo.getDefault(
                Calendar.getInstance().timeInMillis,
                channelName.trim(),
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
            runCatching { updateChannelUseCase.execute(channelInfo.copy(notifyWhenLive = !channelInfo.notifyWhenLive)) }
        }
    }

    override fun onDeleteChannelClicked(channelInfo: ChannelInfo) {
        viewModelScope.launch {
            deleteChannelUseCase.execute(channelInfo)
        }
    }
}