package com.london.designsystem.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.london.designsystem.color.LightNovixColors
import com.london.designsystem.color.LinearGradientDark
import com.london.designsystem.color.LinearGradientLight
import com.london.designsystem.color.NovixColors
import com.london.designsystem.typography.NovixTypography
import com.london.designsystem.typography.NovixTypographySet


object NovixTheme {
    val colors: NovixColors
        @Composable @ReadOnlyComposable get() = LocalNovixColors.current

    val typography: NovixTypographySet
        @Composable @ReadOnlyComposable get() = LocalNovixTypography.current
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
val LocalNovixTypography = staticCompositionLocalOf { NovixTypography }

val horizontalGradient = listOf(
    LinearGradientLight.copy(alpha = 1f),
    LinearGradientLight.copy(alpha = 0.8f),
    LinearGradientLight.copy(alpha = 0.7f),
    LinearGradientDark
)
