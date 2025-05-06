package io.github.u1tramarinet.easysettings.infra

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class SystemSettingHandlerImpl @Inject constructor(@ApplicationContext context: Context) :
    SystemSettingHandler {
    private val contentResolver = context.contentResolver
    private val handler = Handler(Looper.getMainLooper())
    private val observers = mutableListOf<ContentObserver>()

    override fun setScreenBrightness(brightness: Int): Boolean {
        return putInt(
            Settings.System.SCREEN_BRIGHTNESS,
            brightness.coerceIn(getScreenBrightnessRange()),
        )
    }

    override fun getScreenBrightness(): Int {
        return getInt(
            Settings.System.SCREEN_BRIGHTNESS
        ) ?: getScreenBrightnessRange().average().toInt()
    }

    override fun getScreenBrightnessStream(): Flow<Int> = callbackFlow {
        val observer = registerListener(name = Settings.System.SCREEN_BRIGHTNESS) {
            trySend(getScreenBrightness())
        }
        awaitClose {
            unregisterListener(observer)
        }
    }

    override fun getScreenBrightnessRange(): IntRange {
        return 1..255
    }

    override fun registerScreenBrightnessListener(listener: (Int) -> Unit) {
        registerListener(name = Settings.System.SCREEN_BRIGHTNESS) {
            val latestValue = getScreenBrightness()
            listener(latestValue)
        }
    }

    override fun unregisterScreenBrightnessListener() {
        observers.forEach { observer ->
            unregisterListener(observer)
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

    private fun registerListener(name: String, listener: () -> Unit): ContentObserver {
        val newObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                listener()
            }
        }
        contentResolver.registerContentObserver(
            Settings.System.getUriFor(name),
            true,
            newObserver
        )
        observers += newObserver
        return newObserver
    }

    private fun unregisterListener(observer: ContentObserver) {
        contentResolver.unregisterContentObserver(observer)
    }
}