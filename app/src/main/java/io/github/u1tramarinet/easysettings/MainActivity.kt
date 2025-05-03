package io.github.u1tramarinet.easysettings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import io.github.u1tramarinet.easysettings.infra.SystemSettingHandlerImpl
import io.github.u1tramarinet.easysettings.ui.screen.MainScreen
import io.github.u1tramarinet.easysettings.ui.theme.EasySettingsTheme

class MainActivity : ComponentActivity() {
    private var isGrantState by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateGrantState()

        enableEdgeToEdge()
        setContent {
            EasySettingsTheme {
                val handler = remember { SystemSettingHandlerImpl(this) }

                MainScreen(
                    isGrant = isGrantState,
                    handler = handler,
                    onClickSettings = ::jumpToAppSettings
                )
            }
        }
    }

    override fun onResume() {
        Log.d(MainActivity::class.java.simpleName, "onResume")
        super.onResume()
        updateGrantState()
    }

    private fun updateGrantState() {
        isGrantState = Settings.System.canWrite(this)
        Log.d(MainActivity::class.java.simpleName, "updateGrantState: $isGrantState")
    }

    private fun jumpToAppSettings() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = ("package:$packageName").toUri()
        startActivity(intent)
    }
}
