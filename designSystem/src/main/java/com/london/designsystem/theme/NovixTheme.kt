package com.london.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.london.designsystem.color.DarkNovixColors
import com.london.designsystem.color.LightNovixColors
import com.london.designsystem.typography.NovixTypography

@Composable
fun NovixTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (isDarkMode) DarkNovixColors else LightNovixColors

    CompositionLocalProvider(
        LocalNovixColors provides colors,
        LocalNovixTypography provides NovixTypography,
    ) {
        content()
    }
}
