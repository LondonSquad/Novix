package com.london.presentation.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.NovixChip
import com.london.designsystem.utils.shimmerEffect

@Composable
fun <T> GenresSection(
    genres: List<T>,
    screenWidth: Dp,
    selectedGenreId: Int?,
    onGenreClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    getGenreId: (T) -> Int,
    isLoading: Boolean = false,
    getGenreName: @Composable (T) -> String
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier.requiredWidth(screenWidth)
    ) {
        items(genres) { genre ->
            if (!isLoading)
                NovixChip(
                    text = getGenreName(genre),
                    isSelected = (getGenreId(genre) == selectedGenreId),
                    onClick = { onGenreClick(genre) }
                )
            else
                Box(modifier = Modifier.height(40.dp)
                    .width(60.dp)
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(12))
                    .shimmerEffect()
                )
        }
    }
}