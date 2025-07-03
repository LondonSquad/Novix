package com.london.designsystem.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews

@Composable
fun HomeCard(
    imageUrl: Any,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSaved: Boolean = false,
    imageDescription: String? = null,
) {
    var isLoading by remember { mutableStateOf(false) }
    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(imageUrl)
        .crossfade(true)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(158f / 210f)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = NovixTheme.colors.stroke,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularLoadingAnimation(modifier = Modifier.align(Alignment.Center))
        }
        AsyncImage(
            model = imageRequest,
            contentDescription = imageDescription,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
            onLoading = { isLoading = true },
            onSuccess = { isLoading = false },
            onError = { isLoading = false },
        )
        SaveIcon(
            isSaved = isSaved,
            onSaveClick = { onSaveClick() },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopStart)
        )
    }
}

@Composable
private fun CircularLoadingAnimation(
    modifier: Modifier = Modifier,
    color: Color = NovixTheme.colors.secondary
) {
    val infiniteTransition = rememberInfiniteTransition(label = "circular_loading")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Canvas(
        modifier = modifier
            .size(32.dp)
            .rotate(rotation)
    ) {
        val strokeWidth = 3.dp.toPx()

        drawArc(
            color = color,
            startAngle = 0f,
            sweepAngle = 300f,
            useCenter = false,
            style = Stroke(
                width = strokeWidth,
                cap = StrokeCap.Round
            )
        )
    }
}

@ThemePreviews
@Composable
fun HomeCardPreview() {
    NovixTheme {
        HomeCard(
            imageUrl = "https://image.tmdb.org/t/p/w500/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
            isSaved = false,
            onSaveClick = {}
        )
    }
}