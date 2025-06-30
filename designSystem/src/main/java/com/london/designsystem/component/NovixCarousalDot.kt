package com.london.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

@Composable
fun NovixCarousalRow(
    dotsStates: List<Boolean>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(dotsStates) { dot ->
            NovixCarousalDot(
                isSelected = dot
            )
        }
    }
}

@Composable
private fun NovixCarousalDot(
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val dotTintColor by animateColorAsState(
        targetValue = if (isSelected) NovixTheme.colors.primary
        else NovixTheme.colors.onPrimaryHint,
        animationSpec = tween(400)
    )

    val dotSize by animateDpAsState(
        targetValue = if (isSelected) 8.dp else 5.dp,
        animationSpec = tween(400)
    )

    Box(
        modifier = modifier
            .background(
                color = dotTintColor,
                shape = CircleShape
            )
            .size(dotSize)
            .then(
                if (!isSelected)
                    Modifier.border(
                        width = 0.5.dp,
                        color = NovixTheme.colors.stroke,
                        shape = CircleShape
                    ) else Modifier
            )
    )
}

@ThemePreviews
@Composable
fun PreviewCarousalDot() {
    NovixTheme {
        NovixCarousalRow(
            dotsStates = listOf(false, true, false, false, false, false, false, false),
            modifier = Modifier.width(44.dp)
        )
    }
}