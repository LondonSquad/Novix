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
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.shared.genre.TvShowGenreUi

@Composable
fun MediaGenreFilters(
    isMovieSelected: Boolean,
    isTvShowSelected: Boolean,
    selectedMovieGenre: MovieGenreUi,
    selectedTvShowGenre: TvShowGenreUi,
    onMovieGenreClick: (MovieGenreUi) -> Unit,
    onTvShowGenreClick: (TvShowGenreUi) -> Unit,
) {
    val screenWidth = with(LocalDensity.current) {
        LocalWindowInfo.current.containerSize.width.toDp()
    }
    when {
        isMovieSelected -> GenreChipsRow(
            genres = MovieGenreUi.getList(),
            selectedGenre = selectedMovieGenre,
            onGenreClick = onMovieGenreClick,
            screenWidth = screenWidth,
            getText = { stringResource(it.stringResId) }
        )

        isTvShowSelected -> GenreChipsRow(
            genres = TvShowGenreUi.getList(),
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
        selectedMovieGenre = MovieGenreUi.All,
        selectedTvShowGenre = TvShowGenreUi.All,
        onMovieGenreClick = {},
        onTvShowGenreClick = {}
    )
}