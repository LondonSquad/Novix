package com.london.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.RatingBar
import com.london.designsystem.theme.NovixTheme

@Composable
fun RatingItem(
    modifier: Modifier = Modifier,
    rating: Int,
    voteAverage: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        RatingBar(
            modifier = Modifier.size(12.dp),
            rating = rating,
            onRatingChanged = {},
            maxRating = 1
        )

        Text(
            text = voteAverage,
            style = NovixTheme.typography.label.small,
            color = NovixTheme.colors.title
        )
    }
}