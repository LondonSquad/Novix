package com.london.presentation.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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

/**
 * Horizontal scrollable row of movie & TV show genre filter chips
 */
@Composable
fun MediaGenreFilters(
    isMovieSelected: Boolean,
    isTvSelected: Boolean,
    selectedMovieGenre: MovieGenre,
    selectedTvShowGenre: TvShowGenre,
    onMovieGenreClick: (MovieGenre) -> Unit,
    onTvShowGenreClick: (TvShowGenre) -> Unit,
    screenWidth: Dp
) {
    when {
        isMovieSelected -> MovieGenreChipsRow(
            onGenreClick = onMovieGenreClick,
            selectedGenre = selectedMovieGenre,
            screenWidth = screenWidth
        )

        isTvSelected -> TvShowGenreChipsRow(
            onGenreClick = onTvShowGenreClick,
            selectedGenre = selectedTvShowGenre,
            screenWidth = screenWidth
        )
    }
}

/**
 * Horizontal scrollable row of movie genre filter chips
 */
@Composable
fun MovieGenreChipsRow(
    onGenreClick: (MovieGenre) -> Unit,
    selectedGenre: MovieGenre,
    screenWidth: Dp,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier
            .requiredWidth(screenWidth)
            .padding(vertical = 12.dp)
    ) {
        items(MovieGenre.entries.toTypedArray()) { genre ->
            NovixChip(
                text = stringResource(genre.stringResId),
                isSelected = genre == selectedGenre,
                onClick = { onGenreClick(genre) }
            )
        }
    }
}

/**
 * Horizontal scrollable row of TV show genre filter chips
 */
@Composable
fun TvShowGenreChipsRow(
    onGenreClick: (TvShowGenre) -> Unit,
    selectedGenre: TvShowGenre,
    screenWidth: Dp,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier
            .requiredWidth(screenWidth)
            .padding(vertical = 12.dp)
    ) {
        items(TvShowGenre.entries.toTypedArray()) { genre ->
            NovixChip(
                text = stringResource(genre.stringResId),
                isSelected = genre == selectedGenre,
                onClick = { onGenreClick(genre) }
            )
        }
    }
}
