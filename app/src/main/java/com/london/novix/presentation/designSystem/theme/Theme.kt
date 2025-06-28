package com.london.novix.presentation.designSystem.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.london.novix.presentation.designSystem.color.LightNovixColors
import com.london.novix.presentation.designSystem.color.NovixColors
import com.london.novix.presentation.designSystem.shape.NovixShapes
import com.london.novix.presentation.designSystem.shape.NovixThemeShape
import com.london.novix.presentation.designSystem.typography.NovixTextStyle
import com.london.novix.presentation.designSystem.typography.NovixTypography


object NovixTheme {
    val colors: NovixColors
        @Composable @ReadOnlyComposable get() = LocalNovixColors.current

    val typography: NovixTypography
        @Composable @ReadOnlyComposable get() = LocalNovixTypography.current

    val shapes: NovixShapes
        @Composable @ReadOnlyComposable get() = LocalNovixShape.current
}

@Composable
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}

val LocalNovixColors = staticCompositionLocalOf { LightNovixColors }
val LocalNovixTypography = staticCompositionLocalOf { NovixTextStyle }
val LocalNovixShape = staticCompositionLocalOf { NovixThemeShape }