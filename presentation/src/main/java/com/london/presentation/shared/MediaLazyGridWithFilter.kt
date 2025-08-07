package com.london.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Movie
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

@Composable
fun <T : Any> MediaLazyGridWithFilter(
    getImageUrl: (T) -> String,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    items: List<T>? = null,
    pagingItems: LazyPagingItems<T>? = null,
    onSaveClick: (T) -> Unit = {},
    isItemSaved: (T) -> Boolean = { false },
    isMovieSelected: Boolean = true,
    isTvShowSelected: Boolean = false,
    selectedMovieGenre: MovieGenre = MovieGenre.All,
    selectedTvShowGenre: TvShowGenre = TvShowGenre.All,
    onMovieGenreClick: (MovieGenre) -> Unit = {},
    onTvShowGenreClick: (TvShowGenre) -> Unit = {}
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {
        MediaGenreFilters(
            isMovieSelected = isMovieSelected,
            isTvShowSelected = isTvShowSelected,
            selectedMovieGenre = selectedMovieGenre,
            selectedTvShowGenre = selectedTvShowGenre,
            onMovieGenreClick = onMovieGenreClick,
            onTvShowGenreClick = onTvShowGenreClick,
        )

        MediaLazyVerticalGrid(
            items = items,
            pagingItems = pagingItems,
            getImageUrl = getImageUrl,
            onItemClick = onItemClick,
            modifier = Modifier.fillMaxSize(),
            onSaveClick = onSaveClick,
            isItemSaved = isItemSaved,
        )
    }
}

@ThemePreviews
@Composable
private fun Preview() {
    val sampleMovies = listOf(
        Movie(
            id = 1,
            name = "Movie One",
            posterUrl = "https://example.com/movie1.jpg",
            releaseYear = 2023,
            rating = 8,
            genreIds = listOf(28, 12)
        ),
        Movie(
            id = 2,
            name = "Movie Two",
            posterUrl = "https://example.com/movie2.jpg",
            releaseYear = 2024,
            rating = 7,
            genreIds = listOf(18, 35)
        )
    )

    MediaLazyGridWithFilter(
        items = sampleMovies,
        getImageUrl = { it.posterUrl },
        onItemClick = {},
        onSaveClick = {},
        isItemSaved = { false },
        isMovieSelected = true,
        isTvShowSelected = false,
        selectedMovieGenre = MovieGenre.Action,
        selectedTvShowGenre = TvShowGenre.All,
        onMovieGenreClick = {},
        onTvShowGenreClick = {}
    )
}
