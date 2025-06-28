package com.london.novix.presentation.designSystem.typography

import androidx.compose.ui.text.TextStyle

data class NovixTypographySet(
    val headline: TextStyleGroup,
    val title: TextStyleGroup,
    val body: TextStyleGroup,
    val label: TextStyleGroup
)

data class TextStyleGroup(
    val small: TextStyle,
    val medium: TextStyle,
    val large: TextStyle,
)

