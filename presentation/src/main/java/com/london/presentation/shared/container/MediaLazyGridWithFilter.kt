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
import com.london.presentation.shared.MediaGenreFilters
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre
import com.london.presentation.shared.container.MediaGridConfig

@Composable
fun <T : Any> MediaLazyGridWithFilter(
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    imageUrl: (T) -> String = { it.getImageUrl() },
    name: (T) -> String = { it.getName() },
    items: List<T>? = null,
    pagingItems: LazyPagingItems<T>? = null,
    onSaveClick: (T) -> Unit = {},
    isItemSaved: (T) -> Boolean = { false },
    onDeleteClick: (T) -> Unit = {},
    onMovieGenreClick: (MovieGenre) -> Unit = {},
    onTvShowGenreClick: (TvShowGenre) -> Unit = {},
    topBar: @Composable (() -> Unit)? = null,
    config: MediaGridConfig = MediaGridConfig()
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
            items != null -> {
                MediaLazyVerticalGrid(
                    items = items,
                    onNavigateToMovie = { id -> onItemClick(items.first { it is Movie && it.id == id }) },
                    onNavigateToTvShow = { id -> onItemClick(items.first { it is TvShow && it.id == id }) },
                    imageUrl = imageUrl,
                    name = name,
                    hasSaveIcon = config.showSaveIcon,
                    onSaveClick = onSaveClick,
                    isItemSaved = isItemSaved,
                    onDeleteClick = onDeleteClick,
                    isDarkMode = config.isDarkMode,
                    myRatingList = config.myRatingList,
                    rate = config.rate,
                    topBar = topBar,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            pagingItems != null -> {
                MediaLazyVerticalGrid(
                    pagingItems = pagingItems,
                    onNavigateToMovie = { id -> 
                        val item = pagingItems.itemSnapshotList.find { it is Movie && it.id == id }
                        item?.let { onItemClick(it) }
                    },
                    onNavigateToTvShow = { id -> 
                        val item = pagingItems.itemSnapshotList.find { it is TvShow && it.id == id }
                        item?.let { onItemClick(it) }
                    },
                    imageUrl = imageUrl,
                    name = name,
                    hasSaveIcon = config.showSaveIcon,
                    onSaveClick = onSaveClick,
                    isItemSaved = isItemSaved,
                    onDeleteClick = onDeleteClick,
                    isDarkMode = config.isDarkMode,
                    myRatingList = config.myRatingList,
                    rate = config.rate,
                    topBar = topBar,
                    modifier = Modifier.fillMaxSize(),
                )
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
        onItemClick = {},
        onSaveClick = {},
        isItemSaved = { false },
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
            selectedTvShowGenre = TvShowGenre.All
        )
    )
}
