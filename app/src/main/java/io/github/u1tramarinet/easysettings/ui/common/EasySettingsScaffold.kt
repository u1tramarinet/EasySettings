package io.github.u1tramarinet.easysettings.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.u1tramarinet.easysettings.R

@Composable
fun EasySettingsScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable (() -> Unit) = {
        EasySettingsTopAppBar(titleRes = R.string.app_name)
    },
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 32.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(contentPadding),
            content = content,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EasySettingsScaffoldPreview() {
    EasySettingsPreview {
        EasySettingsScaffold {
            Box(modifier = Modifier.fillMaxSize().background(color = Color.Blue))
        }
    }
}