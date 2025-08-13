package com.london.presentation.shared.container

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Movie
import com.london.presentation.shared.EmptyGenreLayout
import com.london.presentation.shared.MediaGenreFilters
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre
import com.london.presentation.utils.isEmpty

@Composable
fun <T : Any> MediaLazyGridWithFilter(
    modifier: Modifier = Modifier,
    imageUrl: (T) -> String = { it.getImageUrl() },
    name: (T) -> String = { it.getName() },
    items: List<T>? = null,
    isLoading: Boolean = false,
    pagingItems: LazyPagingItems<T>? = null,
    onMovieGenreClick: (MovieGenre) -> Unit = {},
    onTvShowGenreClick: (TvShowGenre) -> Unit = {},
    config: MediaGridConfig = MediaGridConfig(),
    topBar: @Composable (() -> Unit)? = null
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {
        topBar?.invoke()

        MediaGenreFilters(
            isMovieSelected = config.isMovieSelected,
            isTvShowSelected = config.isTvShowSelected,
            selectedMovieGenre = config.selectedMovieGenre,
            selectedTvShowGenre = config.selectedTvShowGenre,
            onMovieGenreClick = onMovieGenreClick,
            onTvShowGenreClick = onTvShowGenreClick,
        )

        when {
            !items.isNullOrEmpty() -> {
                MediaLazyVerticalGrid(
                    items = items,
                    imageUrl = imageUrl,
                    name = name,
                    hasSaveIcon = config.showSaveIcon,
                    onSaveClick = { config.onSaveClick(it) },
                    isItemSaved = { config.isItemSaved(it) },
                    onDeleteClick = { config.onDeleteClick(it) },
                    isDarkMode = config.isDarkMode,
                    myRatingList = config.myRatingList,
                    rate = config.rate,
                    topBar = topBar,
                    modifier = Modifier.fillMaxSize(),
                    onNavigateToMovie = config.onNavigateToMovie,
                    onNavigateToTvShow = config.onNavigateToTvShow
                )
            }

            pagingItems != null && pagingItems.itemCount > 0 -> {
                MediaLazyVerticalGrid(
                    pagingItems = pagingItems,
                    imageUrl = imageUrl,
                    name = name,
                    hasSaveIcon = config.showSaveIcon,
                    onSaveClick = { config.onSaveClick(it) },
                    isItemSaved = { config.isItemSaved(it) },
                    onDeleteClick = { config.onDeleteClick(it) },
                    isDarkMode = config.isDarkMode,
                    myRatingList = config.myRatingList,
                    rate = config.rate,
                    topBar = topBar,
                    modifier = Modifier.fillMaxSize(),
                    onNavigateToMovie = config.onNavigateToMovie,
                    onNavigateToTvShow = config.onNavigateToTvShow
                )
            }

            else -> {
                if ((items.isNullOrEmpty() || pagingItems?.isEmpty() == true) && !isLoading) {
                    EmptyGenreLayout()
                }
            }
        }
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
        onMovieGenreClick = {},
        onTvShowGenreClick = {},
        config = MediaGridConfig(
            showSaveIcon = true,
            isDarkMode = true,
            myRatingList = false,
            rate = "3",
            isMovieSelected = true,
            isTvShowSelected = false,
            selectedMovieGenre = MovieGenre.Action,
            selectedTvShowGenre = TvShowGenre.All,
            onNavigateToMovie = {},
            onNavigateToTvShow = {},
            onSaveClick = {},
            isItemSaved = { false },
            onDeleteClick = {}
        )
    )
}
