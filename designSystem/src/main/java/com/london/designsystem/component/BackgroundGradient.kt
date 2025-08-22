package com.london.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.london.designsystem.theme.NovixTheme

@Composable
fun BackgroundGradient(
    modifier: Modifier = Modifier
) {
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                boxSize = coordinates.size
            }
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        NovixTheme.colors.primary.copy(alpha = 0.08f),
                        Color.Transparent,
                        Color.Transparent,
                        NovixTheme.colors.primary.copy(alpha = 0.04f),
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(boxSize.width.toFloat(), boxSize.height.toFloat())
                )
            )
    )
}
