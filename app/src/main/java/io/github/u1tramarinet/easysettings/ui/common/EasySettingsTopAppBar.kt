package io.github.u1tramarinet.easysettings.ui.common

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EasySettingsTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onBack: (() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(text = title)
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EasySettingsTopAppBar(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    onBack: (() -> Unit)? = null
) {
    EasySettingsTopAppBar(
        modifier = modifier,
        title = stringResource(titleRes),
        onBack = onBack,
    )
}

@Preview(showBackground = true)
@Composable
fun EasySettingsTopAppBarPreview() {
    EasySettingsPreview {
        EasySettingsTopAppBar(title = "Settings")
        EasySettingsTopAppBar(title = "Settings", onBack = {})
    }
}