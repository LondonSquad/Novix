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

enum class AppTheme {
    LIGHT,
    DARK;

    fun isDark(): Boolean = this == DARK

    companion object {
        fun fromString(value: String): AppTheme = when (value) {
            DARK.name -> DARK
            else -> LIGHT
        }
    }
}


object NovixTheme {
    val colors: NovixColors
        @Composable @ReadOnlyComposable get() = LocalNovixColors.current

    val typography: NovixTypographySet
        @Composable @ReadOnlyComposable get() = LocalNovixTypography.current

    val theme: AppTheme
        @Composable @ReadOnlyComposable get() = LocalAppTheme.current
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

val LocalAppTheme = staticCompositionLocalOf { AppTheme.DARK }

val horizontalGradient = listOf(
    LinearGradientLight.copy(alpha = 1f),
    LinearGradientLight.copy(alpha = 0.8f),
    LinearGradientLight.copy(alpha = 0.7f),
    LinearGradientDark
)
