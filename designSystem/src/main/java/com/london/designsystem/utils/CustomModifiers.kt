package com.london.designsystem.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

fun Modifier.topBorder(
    color: Color,
    width: Dp,
) = this.drawWithContent {
    drawContent()
    drawLine(
        color = color,
        start = Offset(x = 0f, y = width.toPx() / 2),
        end = Offset(x = size.width, y = width.toPx() / 2),
        strokeWidth = width.toPx(),
    )
}