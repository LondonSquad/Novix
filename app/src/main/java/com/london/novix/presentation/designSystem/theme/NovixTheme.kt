package com.london.novix.presentation.designSystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.london.novix.presentation.designSystem.color.DarkNovixColors
import com.london.novix.presentation.designSystem.color.LightNovixColors
import com.london.novix.presentation.designSystem.shape.NovixThemeShape
import com.london.novix.presentation.designSystem.typography.NovixTextStyle

@Composable
fun NovixTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (isDarkMode) DarkNovixColors else LightNovixColors

    CompositionLocalProvider(
        LocalNovixColors provides colors,
        LocalNovixTypography provides NovixTextStyle,
        LocalNovixShape provides NovixThemeShape
    ) {
        content()
    }
}
