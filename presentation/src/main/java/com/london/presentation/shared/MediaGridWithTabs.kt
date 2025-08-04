package com.london.presentation.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.presentation.R
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre

/**
 * MediaGridWithTabs for regular lists (Continue Watching, Watching History)
 * Uses List<Movie> and List<TvShow> data sources
 */
@Composable
fun MediaGridWithTabs(
    screenTitle: Int,
    movies: List<Movie>,
    tvShows: List<TvShow>,
    tabSelected: Int,
    selectedMovieGenre: MovieGenre,
    selectedTvShowGenre: TvShowGenre,
    isMovieSelected: Boolean,
    isTvSelected: Boolean,
    onBackClick: () -> Unit,
    onTabSelected: (Int) -> Unit,
    onMovieGenreClick: (MovieGenre) -> Unit,
    onTvShowGenreClick: (TvShowGenre) -> Unit,
    onMovieClick: (Int) -> Unit,
    onTvShowClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    emptyTitle: String = "",
    emptyImage: Int? = null
) {
    MediaGridLayout(
        screenTitle = screenTitle,
        tabSelected = tabSelected,
        selectedMovieGenre = selectedMovieGenre,
        selectedTvShowGenre = selectedTvShowGenre,
        isMovieSelected = isMovieSelected,
        isTvSelected = isTvSelected,
        onBackClick = onBackClick,
        onTabSelected = onTabSelected,
        onMovieGenreClick = onMovieGenreClick,
        onTvShowGenreClick = onTvShowGenreClick,
        onMovieClick = onMovieClick,
        onTvShowClick = onTvShowClick,
        modifier = modifier,
        movies = movies,
        tvShows = tvShows,
        isPaging = false,
        isLoading = isLoading,
        emptyTitle = emptyTitle,
        emptyImage = emptyImage
    )
}

/**
 * MediaGridWithTabsPaging for paging data (Top Rated)
 * Uses LazyPagingItems for infinite scrolling
 */
@Composable
fun MediaGridWithTabsPaging(
    screenTitle: Int,
    moviesPagingItems: LazyPagingItems<*>,
    tvShowsPagingItems: LazyPagingItems<*>,
    tabSelected: Int,
    selectedMovieGenre: MovieGenre,
    selectedTvShowGenre: TvShowGenre,
    isMovieSelected: Boolean,
    isTvSelected: Boolean,
    onBackClick: () -> Unit,
    onTabSelected: (Int) -> Unit,
    onMovieGenreClick: (MovieGenre) -> Unit,
    onTvShowGenreClick: (TvShowGenre) -> Unit,
    onMovieClick: (Int) -> Unit,
    onTvShowClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    emptyTitle: String = "",
    emptyImage: Int? = null
) {
    MediaGridLayout(
        screenTitle = screenTitle,
        tabSelected = tabSelected,
        selectedMovieGenre = selectedMovieGenre,
        selectedTvShowGenre = selectedTvShowGenre,
        isMovieSelected = isMovieSelected,
        isTvSelected = isTvSelected,
        onBackClick = onBackClick,
        onTabSelected = onTabSelected,
        onMovieGenreClick = onMovieGenreClick,
        onTvShowGenreClick = onTvShowGenreClick,
        onMovieClick = onMovieClick,
        onTvShowClick = onTvShowClick,
        modifier = modifier,
        moviesPagingItems = moviesPagingItems,
        tvShowsPagingItems = tvShowsPagingItems,
        isPaging = true,
        isLoading = isLoading,
        emptyTitle = emptyTitle,
        emptyImage = emptyImage
    )
}

@ThemePreviews
@Composable
private fun Preview() {
    MediaGridWithTabs(
        screenTitle = R.string.continue_watch,
        tabSelected = 0,
        selectedMovieGenre = MovieGenre.All,
        selectedTvShowGenre = TvShowGenre.All,
        isMovieSelected = true,
        isTvSelected = false,
        onBackClick = {},
        onTabSelected = {},
        onMovieGenreClick = {},
        onTvShowGenreClick = {},
        onMovieClick = {},
        onTvShowClick = {},
        movies = emptyList(),
        tvShows = emptyList(),
        isLoading = false,
        emptyTitle = "No content found",
        emptyImage = null
    )
}
