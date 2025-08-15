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
    items: List<T>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    name: (T) -> String = { it.getName() },
    imageUrl: (T) -> String? = { it.getImageUrl() },
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
            items.isNotEmpty() -> {
                val filteredItems = filterItemsByCategory(items, config)
                RenderFilteredItemsGrid(
                    filteredItems = filteredItems,
                    imageUrl = imageUrl,
                    name = name,
                    config = config,
                    topBar = topBar
                )
            }

            else -> {
                if (!isLoading) {
                    EmptyGenreLayout()
                }
            }
        }
    }
}

@Composable
fun <T : Any> MediaLazyGridWithFilter(
    pagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    name: (T) -> String = { it.getName() },
    imageUrl: (T) -> String? = { it.getImageUrl() },
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

    	if (pagingItems.itemCount > 0) {
            RenderPagingItemsGrid(
                pagingItems = pagingItems,
                imageUrl = imageUrl,
                name = name,
                config = config,
                topBar = topBar
            )
        } else if (!isLoading) {
            EmptyGenreLayout()
        }
    }
}

@Composable
private fun <T : Any> RenderFilteredItemsGrid(
    filteredItems: List<T>,
    imageUrl: (T) -> String?,
    name: (T) -> String,
    config: MediaGridConfig,
    topBar: @Composable (() -> Unit)?
) {
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

@Composable
private fun <T : Any> RenderPagingItemsGrid(
    pagingItems: LazyPagingItems<T>,
    imageUrl: (T) -> String?,
    name: (T) -> String,
    config: MediaGridConfig,
    topBar: @Composable (() -> Unit)?
) {
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
