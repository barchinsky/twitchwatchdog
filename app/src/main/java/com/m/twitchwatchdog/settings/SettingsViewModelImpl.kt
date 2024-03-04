package com.m.twitchwatchdog.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.m.twitchwatchdog.infrastructure.datasource.settings.model.AppSettings
import com.m.twitchwatchdog.settings.usecase.GetSettingsFlowUseCase
import com.m.twitchwatchdog.settings.usecase.UpdateAppSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModelImpl @Inject constructor(
    getSettingsFlowUseCase: GetSettingsFlowUseCase,
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase,
) : ViewModel(), SettingsViewModel {

    override val state: StateFlow<AppSettings> = getSettingsFlowUseCase.execute()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = AppSettings(0, 23)
        )

    override fun onNotifyRangeChanged(from: Int, to: Int) {
        viewModelScope.launch {
            updateAppSettingsUseCase.execute(
                state.value.copy(
                    checkStartHour = from,
                    checkEndHour = to
                )
            )
        }
    }
}