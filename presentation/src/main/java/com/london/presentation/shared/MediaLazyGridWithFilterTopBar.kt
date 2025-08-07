package com.london.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Movie
import com.london.presentation.R
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

@Composable
fun <T : Any> MediaLazyGridWithFilterTopBar(
    title: String,
    onBack: () -> Unit,
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
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp),
            title = title,
            onBackClick = onBack
        )
        MediaLazyGridWithFilter(
            items = items,
            pagingItems = pagingItems,
            getImageUrl = getImageUrl,
            onItemClick = onItemClick,
            onSaveClick = onSaveClick,
            isItemSaved = isItemSaved,
            isMovieSelected = isMovieSelected,
            isTvShowSelected = isTvShowSelected,
            selectedMovieGenre = selectedMovieGenre,
            selectedTvShowGenre = selectedTvShowGenre,
            onMovieGenreClick = onMovieGenreClick,
            onTvShowGenreClick = onTvShowGenreClick
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

    MediaLazyGridWithFilterTopBar(
        title = stringResource(R.string.continue_watch),
        onBack = {},
        items = sampleMovies,
        getImageUrl = { it.posterUrl },
        onItemClick = {},
        onSaveClick = {},
        isItemSaved = { false },
        isMovieSelected = true,
        isTvShowSelected = false,
        selectedMovieGenre = MovieGenre.All,
        selectedTvShowGenre = TvShowGenre.All,
        onMovieGenreClick = {},
        onTvShowGenreClick = {}
    )
}
