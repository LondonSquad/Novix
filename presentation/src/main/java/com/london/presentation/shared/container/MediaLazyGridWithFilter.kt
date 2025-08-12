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
import com.london.presentation.shared.MediaGenreFilters
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

@Composable
fun <T : Any> MediaLazyGridWithFilter(
    imageUrl: (T) -> String,
    name: (T) -> String,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    items: List<T>? = null,
    pagingItems: LazyPagingItems<T>? = null,
    hasSaveIcon: Boolean = true,
    onSaveClick: (T) -> Unit = {},
    isItemSaved: (T) -> Boolean = { false },
    onDeleteClick: (T) -> Unit = {},
    isDarkMode: Boolean = true,
    myRatingList: Boolean = false,
    rate: String = "3",
    isMovieSelected: Boolean = true,
    isTvShowSelected: Boolean = false,
    selectedMovieGenre: MovieGenre = MovieGenre.All,
    selectedTvShowGenre: TvShowGenre = TvShowGenre.All,
    onMovieGenreClick: (MovieGenre) -> Unit = {},
    onTvShowGenreClick: (TvShowGenre) -> Unit = {},
    topBar: @Composable (() -> Unit)? = null
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {

        topBar?.invoke()

        MediaGenreFilters(
            isMovieSelected = isMovieSelected,
            isTvShowSelected = isTvShowSelected,
            selectedMovieGenre = selectedMovieGenre,
            selectedTvShowGenre = selectedTvShowGenre,
            onMovieGenreClick = onMovieGenreClick,
            onTvShowGenreClick = onTvShowGenreClick,
        )

        when {
            items != null -> {
                MediaLazyVerticalGrid(
                    items = items,
                    imageUrl = imageUrl,
                    name = name,
                    onItemClick = onItemClick,
                    hasSaveIcon = hasSaveIcon,
                    onSaveClick = onSaveClick,
                    isItemSaved = isItemSaved,
                    onDeleteClick = onDeleteClick,
                    isDarkMode = isDarkMode,
                    myRatingList = myRatingList,
                    rate = rate,
                    topBar = topBar,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            pagingItems != null -> {
                MediaLazyVerticalGrid(
                    pagingItems = pagingItems,
                    imageUrl = imageUrl,
                    name = { it.toString() },
                    onItemClick = onItemClick,
                    hasSaveIcon = hasSaveIcon,
                    onSaveClick = onSaveClick,
                    isItemSaved = isItemSaved,
                    onDeleteClick = onDeleteClick,
                    isDarkMode = isDarkMode,
                    myRatingList = myRatingList,
                    rate = rate,
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
        imageUrl = { it.posterUrl },
        name = { it.name },
        onItemClick = {},
        hasSaveIcon = true,
        onSaveClick = {},
        isItemSaved = { false },
        onDeleteClick = {},
        isDarkMode = true,
        myRatingList = false,
        rate = "3",
        isMovieSelected = true,
        isTvShowSelected = false,
        selectedMovieGenre = MovieGenre.Action,
        selectedTvShowGenre = TvShowGenre.All,
        onMovieGenreClick = {},
        onTvShowGenreClick = {}
    )
}
