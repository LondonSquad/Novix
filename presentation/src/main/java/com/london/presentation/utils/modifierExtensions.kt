package com.london.presentation.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import com.london.designsystem.theme.NovixTheme

fun Modifier.offsetLayout() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val yOffsetPx = with(this) { 44.dp.roundToPx() }
    val adjustedHeight = (placeable.height - yOffsetPx).coerceAtLeast(0)

    layout(placeable.width, adjustedHeight) {
        placeable.placeRelative(0, -yOffsetPx)
    }
}

@Composable
fun Modifier.headerDetailsCard() = fillMaxWidth()
    .episodeLayout()
    .padding(start = 16.dp, end = 16.dp)
    .heightIn(min = 158.dp)
    .border(
        width = 1.dp,
        color = NovixTheme.colors.stroke,
        shape = RoundedCornerShape(16.dp)
    )
    .clip(RoundedCornerShape(16.dp))
    .background(NovixTheme.colors.surface)

@Composable
fun Modifier.detailsTopBar(backgroundAlpha: Float) = fillMaxWidth()
    .background(NovixTheme.colors.surface.copy(alpha = backgroundAlpha))
    .padding(horizontal = 16.dp, vertical = 8.dp)

@Composable
fun Modifier.navBarBottomPadding() = padding(bottom = 70.dp).padding(
    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
)

private fun Modifier.episodeLayout() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)

    val yOffsetPx = with(this) { 44.dp.roundToPx() }
    val adjustedHeight = (placeable.height - yOffsetPx).coerceAtLeast(0)

    layout(placeable.width, adjustedHeight) {
        placeable.placeRelative(0, -yOffsetPx)
    }
}
