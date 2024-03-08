package com.m.twitchwatchdog

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.m.twitchwatchdog.dashboard.DashboardScreen
import com.m.twitchwatchdog.dashboard.DashboardViewModel
import com.m.twitchwatchdog.dashboard.DashboardViewModelImpl
import com.m.twitchwatchdog.settings.SettingsViewModel
import com.m.twitchwatchdog.settings.SettingsViewModelImpl
import com.m.twitchwatchdog.ui.theme.TwitchWatchdogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                // TODO
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        checkNotificationsPermission()

        setContent {
            val viewModel: DashboardViewModel by viewModels<DashboardViewModelImpl>()
            val dashboardScreenState by viewModel.state.collectAsState()

            val settingsViewModel: SettingsViewModel by viewModels<SettingsViewModelImpl>()
            val settingsState by settingsViewModel.state.collectAsState()

            TwitchWatchdogTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DashboardScreen(
                        state = dashboardScreenState,
                        appSettings = settingsState,
                        onChannelClicked = viewModel::onChannelClicked,
                        onNotifyWhenLiveClicked = viewModel::onNotifyWhenLiveClicked,
                        onSaveChannelClicked = viewModel::onSaveChannelClicked,
                        onDeleteClicked = viewModel::onDeleteChannelClicked,
                        onNotifyRangeSettingChanged = settingsViewModel::onNotifyRangeChanged,
                        onSwipeToRefresh = viewModel::onSwipeToRefresh,
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancelAll()
    }

    private fun createNotificationChannel() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannel = NotificationChannel(
            "twitch_watchdog_alert",
            "Channel status",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun checkNotificationsPermission() {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TwitchWatchdogTheme {
    }
}