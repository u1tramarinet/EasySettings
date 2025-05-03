package io.github.u1tramarinet.easysettings.infra

interface SystemSettingHandler {
    fun setScreenBrightness(brightness: Int): Boolean
    fun getScreenBrightness(): Int
    fun getScreenBrightnessRange(): IntRange
    fun setScreenBrightnessMode(mode: ScreenBrightnessMode): Boolean
    fun getScreenBrightnessMode(): ScreenBrightnessMode
}