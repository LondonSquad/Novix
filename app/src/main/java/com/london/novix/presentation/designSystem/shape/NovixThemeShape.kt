package com.london.novix.presentation.designSystem.shape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

val NovixThemeShape = NovixShapes(
    extraSmall = NovixShapeDefaults.ExtraSmall,
    small = NovixShapeDefaults.Small,
    medium = NovixShapeDefaults.Medium,
    large = NovixShapeDefaults.Large,
    extraLarge = NovixShapeDefaults.ExtraLarge,
    circle = NovixShapeDefaults.Circle
)

object NovixShapeDefaults {
    val ExtraSmall = RoundedCornerShape(12.dp)
    val Small = RoundedCornerShape(16.dp)
    val Medium = RoundedCornerShape(20.dp)
    val Large = RoundedCornerShape(24.dp)
    val ExtraLarge = RoundedCornerShape(28.dp)
    val Circle = RoundedCornerShape(100.dp)
}