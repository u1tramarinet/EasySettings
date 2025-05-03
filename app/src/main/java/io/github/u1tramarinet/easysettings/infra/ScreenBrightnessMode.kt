package io.github.u1tramarinet.easysettings.infra

import android.provider.Settings

enum class ScreenBrightnessMode(val value: Int) {
    Manual(value = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL),
    Automatic(value = Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC),
    ;

    companion object {
        fun fromValue(value: Int): ScreenBrightnessMode {
            return entries.firstOrNull { it.value == value } ?: Automatic
        }
    }
}