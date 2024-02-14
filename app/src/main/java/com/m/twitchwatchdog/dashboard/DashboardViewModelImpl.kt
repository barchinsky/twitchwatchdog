package com.m.twitchwatchdog.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m.twitchwatchdog.dashboard.model.ChannelInfo
import com.m.twitchwatchdog.dashboard.model.DashboardScreenState
import com.m.twitchwatchdog.dashboard.useCase.AddChannelUseCase
import com.m.twitchwatchdog.dashboard.useCase.DisableChannelAlertUseCase
import com.m.twitchwatchdog.dashboard.useCase.EnableChannelAlertUseCase
import com.m.twitchwatchdog.dashboard.useCase.StoreChannelsInfoUseCase
import com.m.twitchwatchdog.infrastructure.useCase.FetchChannelInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
internal class DashboardViewModelImpl @Inject constructor(
    private val fetchChannelInfoUseCase: FetchChannelInfoUseCase,
    private val storeChannelsInfoUseCase: StoreChannelsInfoUseCase,
    private val enableChannelAlertUseCase: EnableChannelAlertUseCase,
    private val disableChannelAlertUseCase: DisableChannelAlertUseCase,
    private val addChannelUseCase: AddChannelUseCase,
    ) : DashboardViewModel, ViewModel() {

    override val state = MutableStateFlow(DashboardScreenState())

    init {
        viewModelScope.launch {
            runCatching {
                val newChannels = fetchChannelInfoUseCase.execute()

                state.update { it.copy(channels = newChannels) }
            }.onFailure {
                println("Failed to fetch channel info: $it")
            }
        }
    }

    override fun onChannelClicked(channelInfo: ChannelInfo) {
        TODO("Not yet implemented")
    }

    override fun onSaveChannelClicked(channelName: String, notifyWhenLive: Boolean) {
        println("New channel: $channelName notifyWhenLive: $notifyWhenLive")
        viewModelScope.launch {
            val channelInfo = ChannelInfo.getDefault(Calendar.getInstance().timeInMillis, channelName, notifyWhenLive)
            runCatching {
                val channels = addChannelUseCase.execute(channelInfo)

                state.update { it.copy(channels = channels) }
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

            state.update { it.copy(channels = channels) }

            storeChannelsInfoUseCase.execute(channels)

            if (channels.any { it.notifyWhenLive }) {
                enableChannelAlertUseCase.execute()
            } else {
                disableChannelAlertUseCase.execute()
            }
        }
    }
}