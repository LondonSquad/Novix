package com.london.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.NovixChip
import com.london.presentation.utils.Genre

@Composable
fun GenresSection(
    genres: List<Genre>,
    screenWidth: Dp,
    selectedGenreId: Int?,
    onGenreClick: (Genre) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier.requiredWidth(screenWidth)
    ) {
        items(genres) { genre ->
            NovixChip(
                text = genre.name,
                isSelected = (genre.id == selectedGenreId),
                onClick = { onGenreClick(genre) }
            )
        }
    }
}