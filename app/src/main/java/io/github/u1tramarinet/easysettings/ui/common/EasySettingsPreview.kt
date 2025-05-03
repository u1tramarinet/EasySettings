package io.github.u1tramarinet.easysettings.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import io.github.u1tramarinet.easysettings.ui.theme.EasySettingsTheme

@Composable
internal fun EasySettingsPreview(content: @Composable ColumnScope.() -> Unit) {
    EasySettingsTheme {
        Column {
            content()
        }
    }
}