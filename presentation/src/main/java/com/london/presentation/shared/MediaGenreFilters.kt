package com.london.presentation.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.NovixChip
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

@Composable
fun MediaGenreFilters(
    isMovieSelected: Boolean,
    isTvShowSelected: Boolean,
    selectedMovieGenre: MovieGenre,
    selectedTvShowGenre: TvShowGenre,
    onMovieGenreClick: (MovieGenre) -> Unit,
    onTvShowGenreClick: (TvShowGenre) -> Unit,
) {
    val screenWidth = with(LocalDensity.current) {
        LocalWindowInfo.current.containerSize.width.toDp()
    }
    when {
        isMovieSelected -> GenreChipsRow(
            genres = MovieGenre.entries,
            selectedGenre = selectedMovieGenre,
            onGenreClick = onMovieGenreClick,
            screenWidth = screenWidth,
            getText = { stringResource(it.stringResId) }
        )

        isTvShowSelected -> GenreChipsRow(
            genres = TvShowGenre.entries,
            selectedGenre = selectedTvShowGenre,
            onGenreClick = onTvShowGenreClick,
            screenWidth = screenWidth,
            getText = { stringResource(it.stringResId) }
        )
    }
}

@Composable
inline fun <reified T : Enum<T>> GenreChipsRow(
    genres: List<T>,
    selectedGenre: T,
    crossinline onGenreClick: (T) -> Unit,
    screenWidth: Dp,
    noinline getText: @Composable (T) -> String,
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
                text = getText(genre),
                isSelected = genre == selectedGenre,
                onClick = { onGenreClick(genre) }
            )
        }
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
        onTvShowGenreClick = {}
    )
}
