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
import com.london.domain.entity.TvShow
import com.london.presentation.shared.EmptyGenreLayout
import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.MediaGenreFilters
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

@Composable
fun <T : Any> MediaLazyGridWithFilter(
    modifier: Modifier = Modifier,
    imageUrl: (T) -> String? = { it.getImageUrl() },
    name: (T) -> String = { it.getName() },
    items: List<T>? = null,
    isLoading: Boolean = false,
    pagingItems: LazyPagingItems<T>? = null,
    tabSelected: Int = MediaCategory.Movies.ordinal,
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
                val filteredItems = when (tabSelected) {
                    0 -> items.filter { it is Movie }
                    1 -> items.filter { it is TvShow }
                    else -> items
                }

                if (filteredItems.isNotEmpty()) {
                    MediaLazyVerticalGrid(
                        items = filteredItems,
                        imageUrl = imageUrl,
                        name = name,
                        hasSaveIcon = config.showSaveIcon,
                        onSaveClick = { config.onSaveClick(it) },
                        isItemSaved = { config.isItemSaved(it) },
                        onDeleteClick = { config.onDeleteClick(it) },
                        myRatingList = config.myRatingList,
                        rate = config.rate,
                        topBar = topBar,
                        modifier = Modifier.fillMaxSize(),
                        onNavigateToMovie = config.onNavigateToMovie,
                        onNavigateToTvShow = config.onNavigateToTvShow
                    )
                } else {
                    EmptyGenreLayout()
                }
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
                    myRatingList = config.myRatingList,
                    rate = config.rate,
                    topBar = topBar,
                    modifier = Modifier.fillMaxSize(),
                    onNavigateToMovie = config.onNavigateToMovie,
                    onNavigateToTvShow = config.onNavigateToTvShow
                )
            }

            else -> {
                if (!isLoading && items.isNullOrEmpty() && (pagingItems == null || pagingItems.itemCount == 0)) {
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
