package com.london.presentation.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp

fun Modifier.offsetLayout() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val yOffsetPx = with(this) { 44.dp.roundToPx() }
    val adjustedHeight = (placeable.height - yOffsetPx).coerceAtLeast(0)

    layout(placeable.width, adjustedHeight) {
        placeable.placeRelative(0, -yOffsetPx)
    }
}