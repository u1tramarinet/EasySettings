package io.github.u1tramarinet.easysettings.ui.appwidget

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.SquareIconButton
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import io.github.u1tramarinet.easysettings.R
import io.github.u1tramarinet.easysettings.infra.SystemSettingHandler
import io.github.u1tramarinet.easysettings.ui.theme.EasySettingsAppWidgetTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class EasySettingsAppWidget : GlanceAppWidget() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface EasySettingsAppWidgetEntryPoint {
        fun getSystemSettingHandler(): SystemSettingHandler
    }

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        Log.d("EasySettingsAppWidget", "provideGlance: glanceId=$id")
        val entryPoint =
            EntryPointAccessors.fromApplication<EasySettingsAppWidgetEntryPoint>(context.applicationContext)
        val handler = entryPoint.getSystemSettingHandler()

        provideContent {
            val state = rememberEasySettingsAppWidgetUiState(handler)

            DisposableEffect(Unit) {
                onDispose {
                    Log.d("EasySettingsAppWidget", "onDispose")
                    CoroutineScope(Dispatchers.Default).launch {
                        // Composition稼働中はupdateは通らないのでdelayしてみる
                        delay(TimeUnit.SECONDS.toMillis(1))
                        Log.d("EasySettingsAppWidget", "update")
                        EasySettingsAppWidget().update(context, id)
                    }
                }
            }

            EasySettingsAppWidgetTheme {
                EasySettingsAppWidgetContent(state = state)
            }
        }
    }

    @Composable
    private fun EasySettingsAppWidgetContent(
        state: EasySettingsAppWidgetUiState,
    ) {
        Scaffold(
            modifier = GlanceModifier.padding(vertical = 12.dp),
            backgroundColor = GlanceTheme.colors.widgetBackground,
        ) {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(modifier = GlanceModifier.fillMaxWidth()) {
                    Text(
                        stringResource(R.string.percent, 0),
                        style = TextStyle(color = GlanceTheme.colors.tertiary, fontSize = 12.sp),
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text(
                        "${stringResource(R.string.screen_brightness)}: ",
                        style = TextStyle(color = GlanceTheme.colors.primary),
                    )
                    Text(
                        stringResource(R.string.percent, state.brightnessPercent),
                        style = TextStyle(color = GlanceTheme.colors.secondary, fontSize = 24.sp),
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text(
                        stringResource(R.string.percent, 100),
                        style = TextStyle(color = GlanceTheme.colors.tertiary, fontSize = 12.sp),
                    )
                }
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .defaultWeight(),
                    horizontalAlignment = Alignment.Horizontal.CenterHorizontally
                ) {
                    SquareIconButton(
                        modifier = GlanceModifier
                            .defaultWeight()
                            .fillMaxHeight(),
                        imageProvider = ImageProvider(resId = R.drawable.outline_brightness_empty_24),
                        contentDescription = null,
                        onClick = state.clickDown,
                    )
                    Spacer(modifier = GlanceModifier.width(12.dp))
                    SquareIconButton(
                        modifier = GlanceModifier
                            .defaultWeight()
                            .fillMaxHeight(),
                        imageProvider = ImageProvider(resId = R.drawable.outline_brightness_7_24),
                        contentDescription = null,
                        onClick = state.clickUp,
                    )
                }
            }
        }
    }

    @Composable
    private fun stringResource(@StringRes id: Int): String {
        return LocalContext.current.getString(id)
    }

    @Composable
    private fun stringResource(@StringRes id: Int, vararg formatArgs: Any): String {
        return LocalContext.current.getString(id, *formatArgs)
    }
}
