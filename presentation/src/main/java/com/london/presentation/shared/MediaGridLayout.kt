package com.london.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.london.presentation.R
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.EmptyLayout
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.TabItem
import com.london.designsystem.component.TabLayout
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre
import com.london.presentation.utils.gridColmuns
import androidx.compose.ui.tooling.preview.Preview

/**
 * Core layout implementation for both regular lists and paging data
 * Handles the complete screen layout with tabs, filters, and grid
 */
@Composable
fun MediaGridLayout(
    screenTitle: Int,
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
    movies: List<Movie>? = null,
    tvShows: List<TvShow>? = null,
    moviesPagingItems: LazyPagingItems<*>? = null,
    tvShowsPagingItems: LazyPagingItems<*>? = null,
    isPaging: Boolean,
    isLoading: Boolean = false,
    emptyTitle: String = "",
    emptyImage: Int? = null
) {
    val screenWidth = with(LocalDensity.current) {
        LocalWindowInfo.current.containerSize.width.toDp()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp),
            title = stringResource(screenTitle),
            onBackClick = onBackClick
        )

        when {
            isLoading -> CircularLoading(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )

            !isLoading -> {
                TabLayout(
                    tabs = listOf(
                        TabItem(R.string.movies),
                        TabItem(R.string.tv_shows),
                    ),
                    selectedIndex = tabSelected,
                    onTabSelected = onTabSelected,
                    modifier = Modifier.background(NovixTheme.colors.surface)
                )

                MediaGenreFilters(
                    isMovieSelected = isMovieSelected,
                    isTvShowSelected = isTvSelected,
                    selectedMovieGenre = selectedMovieGenre,
                    selectedTvShowGenre = selectedTvShowGenre,
                    onMovieGenreClick = onMovieGenreClick,
                    onTvShowGenreClick = onTvShowGenreClick,
                    screenWidth = screenWidth
                )

                MediaContent(
                    isMovieSelected = isMovieSelected,
                    isPaging = isPaging,
                    movies = movies,
                    tvShows = tvShows,
                    moviesPagingItems = moviesPagingItems,
                    tvShowsPagingItems = tvShowsPagingItems,
                    onMovieClick = onMovieClick,
                    onTvShowClick = onTvShowClick,
                    emptyTitle = emptyTitle,
                    emptyImage = emptyImage
                )
            }
        }
    }
}

@Composable
private fun MediaContent(
    isMovieSelected: Boolean,
    isPaging: Boolean,
    movies: List<Movie>?,
    tvShows: List<TvShow>?,
    moviesPagingItems: LazyPagingItems<*>?,
    tvShowsPagingItems: LazyPagingItems<*>?,
    onMovieClick: (Int) -> Unit,
    onTvShowClick: (Int) -> Unit,
    emptyTitle: String,
    emptyImage: Int?
) {
    val isContentEmpty = checkIfContentEmpty(
        isMovieSelected = isMovieSelected,
        isPaging = isPaging,
        movies = movies,
        tvShows = tvShows,
        moviesPagingItems = moviesPagingItems,
        tvShowsPagingItems = tvShowsPagingItems
    )

    when {
        isContentEmpty && emptyImage != null -> EmptyLayout(
            text = emptyTitle,
            image = emptyImage,
            modifier = Modifier.fillMaxSize()
        )

        else -> MediaGridItems(
            isMovieSelected = isMovieSelected,
            isPaging = isPaging,
            movies = movies,
            tvShows = tvShows,
            moviesPagingItems = moviesPagingItems,
            tvShowsPagingItems = tvShowsPagingItems,
            onMovieClick = onMovieClick,
            onTvShowClick = onTvShowClick
        )
    }
}

private fun checkIfContentEmpty(
    isMovieSelected: Boolean,
    isPaging: Boolean,
    movies: List<Movie>?,
    tvShows: List<TvShow>?,
    moviesPagingItems: LazyPagingItems<*>?,
    tvShowsPagingItems: LazyPagingItems<*>?
): Boolean {
    return when {
        isMovieSelected -> {
            when {
                isPaging -> {
                    val movieCount = moviesPagingItems?.itemCount ?: 0
                    movieCount == 0
                }
                else -> {
                    val movieCount = movies?.size ?: 0
                    movieCount == 0
                }
            }
        }
        else -> {
            when {
                isPaging -> {
                    val tvShowCount = tvShowsPagingItems?.itemCount ?: 0
                    tvShowCount == 0
                }
                else -> {
                    val tvShowCount = tvShows?.size ?: 0
                    tvShowCount == 0
                }
            }
        }
    }
}

/**
 * Grid items that handles both regular lists and paging data
 */
@Composable
fun MediaGridItems(
    isMovieSelected: Boolean,
    isPaging: Boolean,
    movies: List<Movie>?,
    tvShows: List<TvShow>?,
    moviesPagingItems: LazyPagingItems<*>?,
    tvShowsPagingItems: LazyPagingItems<*>?,
    onMovieClick: (Int) -> Unit,
    onTvShowClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridColmuns()),
        contentPadding = PaddingValues(
            top = 12.dp,
            bottom = 16.dp,
            start = 16.dp,
            end = 16.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.background(NovixTheme.colors.surface)
    ) {
        when {
            isMovieSelected -> {
                if (isPaging) {
                    moviesPagingItems?.let { pagingItems ->
                        items(pagingItems.itemCount) { index ->
                            val movie = pagingItems[index]
                            movie?.let { movieItem ->
                                val topRatedMovie =
                                    movieItem as com.london.domain.entity.toprated.TopRatedMovie
                                MovieCard(
                                    imageUrl = topRatedMovie.posterUrl,
                                    onMovieClick = onMovieClick,
                                    movieId = topRatedMovie.id
                                )
                            }
                        }
                    }
                } else {
                    movies?.let { movieList ->
                        items(movieList) { movie ->
                            MovieCard(
                                imageUrl = movie.posterUrl,
                                onMovieClick = onMovieClick,
                                movieId = movie.id
                            )
                        }
                    }
                }
            }

            !isMovieSelected -> {
                if (isPaging) {
                    tvShowsPagingItems?.let { pagingItems ->
                        items(pagingItems.itemCount) { index ->
                            val tvShow = pagingItems[index]
                            tvShow?.let { seriesItem ->
                                val topRatedTvSeries =
                                    seriesItem as com.london.domain.entity.toprated.TopRatedTvSeries
                                TvShowCard(
                                    imageUrl = topRatedTvSeries.posterUrl,
                                    onTvShowClick = onTvShowClick,
                                    tvShowId = topRatedTvSeries.id
                                )
                            }
                        }
                    }
                } else {
                    tvShows?.let { tvShowList ->
                        items(tvShowList) { tvShow ->
                            TvShowCard(
                                imageUrl = tvShow.posterPicture,
                                onTvShowClick = onTvShowClick,
                                tvShowId = tvShow.id
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieCard(
    imageUrl: String,
    onMovieClick: (Int) -> Unit,
    movieId: Int
) {
    HomeCard(
        imageUrl = imageUrl,
        isSaved = false,
        onSaveClick = {
            // TODO: Implement save functionality
        },
        modifier = Modifier.clickable {
            onMovieClick(movieId)
        }
    )
}

@Composable
private fun TvShowCard(
    imageUrl: String,
    onTvShowClick: (Int) -> Unit,
    tvShowId: Int
) {
    HomeCard(
        imageUrl = imageUrl,
        isSaved = false,
        onSaveClick = {
            // TODO: Implement save functionality
        },
        modifier = Modifier.clickable {
            onTvShowClick(tvShowId)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MediaGridLayoutPreview() {
    MediaGridLayout(
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
        isPaging = false,
        isLoading = false,
        emptyTitle = "No content found",
        emptyImage = null
    )
} 