package com.london.presentation.utils

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.utils.painter

@Composable
fun getThemeAwarePainter(
    @DrawableRes lightThemeRes: Int,
    @DrawableRes darkThemeRes: Int
) = (if (NovixTheme.isThemeDark) darkThemeRes else lightThemeRes).painter
