package com.london.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.london.designsystem.theme.NovixTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularLoading(
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wavy")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    val stroke = NovixTheme.colors.stroke
    val primaryColor = NovixTheme.colors.primary

    Canvas(modifier = modifier.size(48.dp)) {
        val radius = size.minDimension / 2 - 8.dp.toPx()
        val center = Offset(size.width / 2, size.height / 2)
        val sweepAngle = 360f * progress

        drawCircle(
            color = stroke,
            radius = radius,
            center = center,
            style = Stroke(4.dp.toPx())
        )

        val path = Path()
        for (angle in 0..sweepAngle.toInt() step 2) {
            val waveRadius = radius + 4f * sin(Math.toRadians(angle * 7.0)).toFloat()
            val radians = Math.toRadians((angle - 90).toDouble())
            val x = center.x + waveRadius * cos(radians).toFloat()
            val y = center.y + waveRadius * sin(radians).toFloat()

            if (angle == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = primaryColor,
            style = Stroke(4.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    CircularLoading()
}