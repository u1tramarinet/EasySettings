package io.github.u1tramarinet.easysettings.ui.appwidget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.u1tramarinet.easysettings.infra.SystemSettingHandler
import io.github.u1tramarinet.easysettings.utils.fromPercentToValue
import io.github.u1tramarinet.easysettings.utils.fromValueToPercent
import kotlinx.coroutines.flow.map
import kotlin.math.max
import kotlin.math.min

data class EasySettingsAppWidgetUiState(
    val brightness: Int,
    val brightnessPercent: Int,
    val clickDown: () -> Unit,
    val clickUp: () -> Unit,
)

@Composable
fun rememberEasySettingsAppWidgetUiState(handler: SystemSettingHandler): EasySettingsAppWidgetUiState {
    val initialBrightness = handler.getScreenBrightness()
    val range = handler.getScreenBrightnessRange()
    var brightness by remember { mutableIntStateOf(initialBrightness) }
    val brightnessPercent by remember(brightness) {
        mutableIntStateOf(brightness.fromValueToPercent(range))
    }
    handler.getScreenBrightnessStream().map {
        brightness = it
    }.collectAsState(initial = initialBrightness)
    return EasySettingsAppWidgetUiState(
        brightness = brightness,
        brightnessPercent = brightnessPercent,
        clickDown = {
            val currentValue = handler.getScreenBrightness()
            val currentPercent = currentValue.fromValueToPercent(range)

            val newPercent = max(currentPercent - 10, 0)
            val newValue = newPercent.fromPercentToValue(range)

            val result = handler.setScreenBrightness(newValue)
            brightness = if (result) newValue else currentValue
        },
        clickUp = {
            val currentValue = handler.getScreenBrightness()
            val currentPercent = currentValue.fromValueToPercent(range)

            val newPercent = min(currentPercent + 10, 100)
            val newValue = newPercent.fromPercentToValue(range)

            val result = handler.setScreenBrightness(newValue)
            brightness = if (result) newValue else currentValue
        }
    )
}
