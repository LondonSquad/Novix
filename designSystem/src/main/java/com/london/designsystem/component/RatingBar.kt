package com.london.designsystem.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.R
import com.london.designsystem.theme.NovixTheme

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    maxRating: Int = 10
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            for (i in 1..maxRating) {
                val isFilled = i <= rating

                val scale by animateFloatAsState(
                    targetValue = if (isFilled) 1.1f else 1.05f,
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = FastOutSlowInEasing
                    ),
                    label = "scale"
                )

                Icon(
                    painter = if (isFilled)
                        painterResource(R.drawable.star_filled)
                    else
                        painterResource(R.drawable.star_outline),
                    contentDescription = "Rate $i",
                    tint = NovixTheme.colors.yellowAccent,
                    modifier = Modifier.padding(end = 8.dp)
                        .size(24.dp)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale
                        )
                        .clickable { onRatingChanged(i) }
                )
            }
        }
    }
}

