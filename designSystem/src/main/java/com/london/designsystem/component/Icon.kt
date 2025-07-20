package com.london.designsystem.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun Icon(
    painter: Painter,
    tint: Color,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    MaterialTheme.shapes.medium
    androidx.compose.material3.Icon(
        painter = painter,
        tint = tint,
        contentDescription = contentDescription,
        modifier = modifier,
    )
}