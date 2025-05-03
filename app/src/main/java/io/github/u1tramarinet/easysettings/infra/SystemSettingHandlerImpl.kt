package io.github.u1tramarinet.easysettings.infra

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.util.Log

class SystemSettingHandlerImpl(context: Context) : SystemSettingHandler {
    private val contentResolver = context.contentResolver
    private val handler = Handler(Looper.getMainLooper())
    private val observers = mutableListOf<ContentObserver>()
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

    override fun registerScreenBrightnessListener(listener: (Int) -> Unit) {
        registerListener(name = Settings.System.SCREEN_BRIGHTNESS) {
            val latestValue = getScreenBrightness()
            listener(latestValue)
        }
    }

    override fun unregisterScreenBrightnessListener() {
        observers.forEach { observer ->
            contentResolver.unregisterContentObserver(observer)
        }
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

    private fun registerListener(name: String, listener: () -> Unit) {
        val newObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                listener()
            }
        }
        contentResolver.registerContentObserver(
            Settings.System.getUriFor(name),
            false,
            newObserver
        )
        observers += newObserver
    }
}