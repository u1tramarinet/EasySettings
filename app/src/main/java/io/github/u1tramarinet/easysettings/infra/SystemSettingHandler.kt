package io.github.u1tramarinet.easysettings.infra

import kotlinx.coroutines.flow.Flow

interface SystemSettingHandler {
    fun setScreenBrightness(brightness: Int): Boolean
    fun getScreenBrightness(): Int
    fun getScreenBrightnessStream(): Flow<Int>
    fun getScreenBrightnessRange(): IntRange
    fun registerScreenBrightnessListener(listener: (Int) -> Unit)
    fun unregisterScreenBrightnessListener()
    fun setScreenBrightnessMode(mode: ScreenBrightnessMode): Boolean
    fun getScreenBrightnessMode(): ScreenBrightnessMode
}