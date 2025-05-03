package io.github.u1tramarinet.easysettings.infra

import android.content.Context
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.util.Log

class SystemSettingHandlerImpl(context: Context) : SystemSettingHandler {
    private val contentResolver = context.contentResolver
    override fun setScreenBrightness(brightness: Int): Boolean {
        if (getScreenBrightnessRange().contains(brightness).not()) {
            return false
        }
        return putInt(
            Settings.System.SCREEN_BRIGHTNESS,
            brightness,
        )
    }

    override fun getScreenBrightness(): Int {
        return getInt(
            Settings.System.SCREEN_BRIGHTNESS
        ) ?: getScreenBrightnessRange().average().toInt()
    }

    override fun getScreenBrightnessRange(): IntRange {
        return 0..255
    }

    override fun setScreenBrightnessMode(mode: ScreenBrightnessMode): Boolean {
        return putInt(
            Settings.System.SCREEN_BRIGHTNESS_MODE,
            mode.value,
        )
    }

    override fun getScreenBrightnessMode(): ScreenBrightnessMode {
        return ScreenBrightnessMode.fromValue(
            getInt(
                Settings.System.SCREEN_BRIGHTNESS_MODE
            ) ?: ScreenBrightnessMode.Automatic.value
        )
    }

    private fun putInt(name: String, value: Int): Boolean {
        return Settings.System.putInt(contentResolver, name, value)
    }

    private fun getInt(name: String): Int? {
        return try {
            Settings.System.getInt(contentResolver, name)
        } catch (e: SettingNotFoundException) {
            Log.e(SystemSettingHandlerImpl::class.java.simpleName, e.stackTraceToString())
            null
        }
    }
}