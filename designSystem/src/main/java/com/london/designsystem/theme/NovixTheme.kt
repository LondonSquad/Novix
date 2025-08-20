package com.london.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.london.designsystem.color.DarkNovixColors
import com.london.designsystem.color.LightNovixColors
import com.london.designsystem.typography.NovixTypography

@Composable
fun NovixTheme(
    isAppDarkMode: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (isAppDarkMode) DarkNovixColors else LightNovixColors

    CompositionLocalProvider(
        LocalNovixColors provides colors,
        LocalNovixTypography provides NovixTypography,
        LocalAppTheme provides isAppDarkMode
    ) {
        content()
    }
}
