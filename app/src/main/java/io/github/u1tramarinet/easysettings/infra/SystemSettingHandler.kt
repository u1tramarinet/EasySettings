package io.github.u1tramarinet.easysettings.infra

interface SystemSettingHandler {
    fun setScreenBrightness(brightness: Int): Boolean
    fun getScreenBrightness(): Int
    fun getScreenBrightnessRange(): IntRange
    fun registerScreenBrightnessListener(listener: (Int) -> Unit)
    fun unregisterScreenBrightnessListener()
    fun setScreenBrightnessMode(mode: ScreenBrightnessMode): Boolean
    fun getScreenBrightnessMode(): ScreenBrightnessMode
}