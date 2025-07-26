package com.london.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.NovixChip
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

@Composable
fun GenresSection(
    genres: List<MovieGenre>,
    screenWidth: Dp,
    selectedGenreId: Int?,
    onGenreClick: (MovieGenre) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier.requiredWidth(screenWidth)
    ) {
        items(genres) { genre ->
            NovixChip(
                text = stringResource(genre.stringResId),
                isSelected = (genre.id == selectedGenreId),
                onClick = { onGenreClick(genre) }
            )
        }
    }
}

@Composable
fun GenresSection(
    genres: List<TvShowGenre>,
    screenWidth: Dp,
    selectedGenreId: Int?,
    onGenreClick: (TvShowGenre) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier.requiredWidth(screenWidth)
    ) {
        items(genres) { genre ->
            NovixChip(
                text = stringResource(genre.stringResId),
                isSelected = (genre.id == selectedGenreId),
                onClick = { onGenreClick(genre) }
            )
        }
    }
}