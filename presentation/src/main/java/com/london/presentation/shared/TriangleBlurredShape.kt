package com.london.presentation.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.london.designsystem.theme.NovixTheme

@Composable
fun TriangleBlurredShape() {
    val triangleBackgroundColor: Color = NovixTheme.colors.primary.copy(alpha = 0.08f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .blur(150.dp)
            .drawWithContent {
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(size.width * 0.7f, 0f)
                    lineTo(0f, size.height * 0.25f)
                    close()
                }
                drawPath(
                    path,
                    color = triangleBackgroundColor,
                )
                drawContent()
            }
    )
}