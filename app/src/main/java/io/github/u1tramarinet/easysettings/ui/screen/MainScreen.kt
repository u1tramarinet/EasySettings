package io.github.u1tramarinet.easysettings.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.u1tramarinet.easysettings.BuildConfig
import io.github.u1tramarinet.easysettings.R
import io.github.u1tramarinet.easysettings.infra.ScreenBrightnessMode
import io.github.u1tramarinet.easysettings.infra.SystemSettingHandler
import io.github.u1tramarinet.easysettings.ui.common.EasySettingsPreview
import io.github.u1tramarinet.easysettings.ui.common.EasySettingsScaffold

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    handler: SystemSettingHandler,
    isGrant: Boolean,
    onClickSettings: () -> Unit,
) {
    if (isGrant) {
        MainScreenContent(
            modifier = modifier,
            handler = handler,
        )
        if (BuildConfig.DEBUG) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp)
            ) {
                SettingButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.BottomCenter),
                    onClickSettings = onClickSettings,
                )
            }
        }
    } else {
        MainScreenGrantContent(
            modifier = modifier,
            onClickSettings = onClickSettings,
        )
    }
}

@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    handler: SystemSettingHandler,
) {
    EasySettingsScaffold(modifier = modifier) {
        Column {
            ScreenBrightness(
                modifier = Modifier.fillMaxWidth(),
                handler = handler,
            )
        }
    }
}

@Composable
private fun MainScreenGrantContent(
    modifier: Modifier = Modifier,
    onClickSettings: () -> Unit,
) {
    EasySettingsScaffold(modifier = modifier) {
        Column {
            Text(text = stringResource(R.string.not_grant_message))
            Spacer(modifier = Modifier.weight(1f))
            SettingButton(
                modifier = Modifier.fillMaxWidth(),
                onClickSettings = onClickSettings,
            )
        }
    }
}

@Composable
private fun ScreenBrightness(
    modifier: Modifier = Modifier,
    handler: SystemSettingHandler,
) {
    var currentMode by remember { mutableStateOf(handler.getScreenBrightnessMode()) }
    var currentValue by remember { mutableIntStateOf(handler.getScreenBrightness()) }
    val valueRange = handler.getScreenBrightnessRange()
    val isAutomatic = currentMode == ScreenBrightnessMode.Automatic
    DisposableEffect(Unit) {
        handler.registerScreenBrightnessListener { latestValue ->
            currentValue = latestValue
        }
        onDispose {
            handler.unregisterScreenBrightnessListener()
        }
    }
    SettingContent(
        modifier = modifier,
        title = "画面輝度",
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(R.string.automatic_adjustment))
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isAutomatic,
                onCheckedChange = { checked ->
                    val after = if (checked) {
                        ScreenBrightnessMode.Automatic
                    } else {
                        ScreenBrightnessMode.Manual
                    }
                    val result = handler.setScreenBrightnessMode(after)
                    if (result) {
                        currentMode = after
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(0.2f),
                text = valueRange.first.toString(),
                textAlign = TextAlign.Center,
            )
            Box(modifier = Modifier.weight(1f)) {
                Slider(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    value = currentValue.toFloat(),
                    valueRange = valueRange.first * 1f..valueRange.last * 1f,
                    enabled = !isAutomatic,
                    onValueChange = { value ->
                        val after = value.toInt()
                        val result = handler.setScreenBrightness(after)
                        if (result) {
                            currentValue = after
                        }
                    },
                )
                Row(
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .fillMaxWidth(),
                ) {
                    Spacer(modifier = Modifier.weight(currentValue - valueRange.first + 0.1f))
                    Text(text = currentValue.toString())
                    Spacer(modifier = Modifier.weight(valueRange.last - currentValue + 0.1f))
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier.weight(0.2f),
                text = valueRange.last.toString(),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun SettingContent(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier) {
        Text(text = title)
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            content = content,
        )
    }
}

@Composable
private fun SettingButton(modifier: Modifier = Modifier, onClickSettings: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = onClickSettings,
    ) {
        Text(text = "設定へ移動する")
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview1() {
    EasySettingsPreview {
        MainScreenContent(
            handler = object : SystemSettingHandler {
                override fun setScreenBrightness(brightness: Int): Boolean {
                    return true
                }

                override fun getScreenBrightness(): Int {
                    return 150
                }

                override fun getScreenBrightnessRange(): IntRange {
                    return 1..255
                }

                override fun registerScreenBrightnessListener(listener: (Int) -> Unit) {
                    // NOP
                }

                override fun unregisterScreenBrightnessListener() {
                    // NOP
                }

                override fun setScreenBrightnessMode(mode: ScreenBrightnessMode): Boolean {
                    return true
                }

                override fun getScreenBrightnessMode(): ScreenBrightnessMode {
                    return ScreenBrightnessMode.Automatic
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview2() {
    EasySettingsPreview {
        MainScreenGrantContent(
            onClickSettings = {},
        )
    }
}
