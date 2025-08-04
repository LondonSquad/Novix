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
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

/**
 * Horizontal scrollable row of movie & TV show genre filter chips
 */
@Composable
fun MediaGenreFilters(
    isMovieSelected: Boolean,
    isTvShowSelected: Boolean,
    selectedMovieGenre: MovieGenre,
    selectedTvShowGenre: TvShowGenre,
    onMovieGenreClick: (MovieGenre) -> Unit,
    onTvShowGenreClick: (TvShowGenre) -> Unit,
    screenWidth: Dp
) {
    when {
        isMovieSelected -> GenreChipsRow(
            genres = MovieGenre.entries.toTypedArray(),
            selectedGenre = selectedMovieGenre,
            onGenreClick = onMovieGenreClick,
            screenWidth = screenWidth
        )

        isTvShowSelected -> GenreChipsRow(
            genres = TvShowGenre.entries.toTypedArray(),
            selectedGenre = selectedTvShowGenre,
            onGenreClick = onTvShowGenreClick,
            screenWidth = screenWidth
        )
    }
}

/**
 * Generic horizontal scrollable row of genre filter chips
 */
@Composable
inline fun <reified T : Enum<T>> GenreChipsRow(
    genres: Array<T>,
    selectedGenre: T,
    crossinline onGenreClick: (T) -> Unit,
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
        items(genres) { genre ->
            NovixChip(
                text = getGenreText(genre),
                isSelected = genre == selectedGenre,
                onClick = { onGenreClick(genre) }
            )
        }
    }
}

/**
 * Get the string resource for the genre
 */
@Composable
inline fun <reified T : Enum<T>> getGenreText(genre: T): String {
    return when (genre) {
        is MovieGenre -> stringResource(genre.stringResId)
        is TvShowGenre -> stringResource(genre.stringResId)
        else -> genre.name
    }
}


@ThemePreviews
@Composable
private fun Preview() {
    MediaGenreFilters(
        isMovieSelected = true,
        isTvShowSelected = false,
        selectedMovieGenre = MovieGenre.All,
        selectedTvShowGenre = TvShowGenre.All,
        onMovieGenreClick = {},
        onTvShowGenreClick = {},
        screenWidth = 360.dp
    )
}

