package com.london.presentation.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.london.designsystem.utils.shimmerEffect

@Composable
fun ShimmerMovieCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(240.dp)
            .clip(RoundedCornerShape(12.dp))
            .shimmerEffect()
    )
}
