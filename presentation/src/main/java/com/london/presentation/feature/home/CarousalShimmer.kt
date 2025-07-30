package com.london.presentation.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.SectionHeader
import com.london.designsystem.utils.shimmerEffect

@Composable
fun CarousalShimmerEffect(
    isLoading: Boolean = true
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionHeader(
            modifier = Modifier,
            text = "",
            isLoading = isLoading,
            hasGetAll = true,
            hasIcon = true,
            onClick = {}
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier
                .height(210.dp)
                .clip(RoundedCornerShape(12.dp))
                .weight(1f)
                .shimmerEffect()
            )

            Box(modifier = Modifier
                .height(210.dp)
                .clip(RoundedCornerShape(12.dp))
                .weight(0.5f)
                .shimmerEffect()
            )

            Box(modifier = Modifier
                .height(240.dp)
                .clip(RoundedCornerShape(12.dp))
                .weight(0.5f)
                .shimmerEffect()
            )
        }
    }
}