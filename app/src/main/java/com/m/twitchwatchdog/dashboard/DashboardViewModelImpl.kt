package com.m.twitchwatchdog.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.model.DashboardScreenState
import com.m.twitchwatchdog.dashboard.usecase.AddChannelUseCase
import com.m.twitchwatchdog.dashboard.usecase.DeleteChannelUseCase
import com.m.twitchwatchdog.dashboard.usecase.DisableChannelAlertUseCase
import com.m.twitchwatchdog.dashboard.usecase.EnableChannelAlertUseCase
import com.m.twitchwatchdog.dashboard.usecase.FetchChannelInfoUseCase
import com.m.twitchwatchdog.dashboard.usecase.GetChannelPreviewUseCase
import com.m.twitchwatchdog.dashboard.usecase.GetChannelsFlowUseCase
import com.m.twitchwatchdog.dashboard.usecase.IsSyncJobRunningUseCase
import com.m.twitchwatchdog.dashboard.usecase.UpdateChannelUseCase
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
    private val getChannelPreviewUseCase: GetChannelPreviewUseCase,
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
                val isSyncJobRunning = isSyncJobRunningUseCase.execute()
                state.update { it.copy(channels = channels, syncJobRunning = isSyncJobRunning) }

                if (channels.isEmpty() || channels.all { !it.notifyWhenLive }) {
                    disableChannelAlertUseCase.execute()
                    state.update { it.copy(syncJobRunning = false) }
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
            }.onFailure {
                println("Failed to add channel: $it")
            }
            state.update { it.copy(channelPreview = null) }
        }
    }

    override fun onNotifyWhenLiveClicked(channelInfo: ChannelInfo) {
        viewModelScope.launch {
            runCatching {
                val newChannelInfo = channelInfo.copy(notifyWhenLive = !channelInfo.notifyWhenLive)
                updateChannelUseCase.execute(newChannelInfo)
                if (newChannelInfo.notifyWhenLive) {
                    enableChannelAlertUseCase.execute()
                }
            }
        }
    }

    override fun onDeleteChannelClicked(channelInfo: ChannelInfo) {
        viewModelScope.launch {
            deleteChannelUseCase.execute(channelInfo)
        }
    }

    override fun onSwipeToRefresh() {
        viewModelScope.launch {
            state.update { it.copy(refreshing = true) }

            try {
                fetchChannelInfoUseCase.execute()
            } catch (t: Throwable) {
                println("Failed to refresh channels: $t")
            } finally {
                state.update { it.copy(refreshing = false) }
            }
        }
    }

    override fun onRequestPreview(channelName: String) {
        viewModelScope.launch {
            runCatching {
                val channelInfo = if (channelName.isNotBlank()) {
                    getChannelPreviewUseCase.execute(channelName)
                } else {
                    null
                }

                state.update { it.copy(channelPreview = channelInfo) }
            }.onFailure {
                println("Failed to fetch channel preview.")
                state.update { it.copy(channelPreview = null) }
            }
        }
    }
}