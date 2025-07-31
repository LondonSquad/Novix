package com.london.presentation.feature.toprated

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
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.R
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.TabItem
import com.london.designsystem.component.TabLayout
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.designsystem.utils.string
import com.london.domain.entity.toprated.TopRatedMovie
import com.london.domain.entity.toprated.TopRatedTvSeries
import com.london.presentation.utils.Listen
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre
import kotlinx.coroutines.flow.flow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TopRatedScreen(
    viewModel: TopRatedViewModel = koinViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateMovie: (Int) -> Unit = {},
    onNavigateTvShow: (Int) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TopRatedEffect.NavigateToMovieDetails -> onNavigateMovie(currentEffect.id)
            is TopRatedEffect.NavigateToTvShowDetails -> onNavigateTvShow(currentEffect.id)
            is TopRatedEffect.NavigateBack -> onNavigateBack()
        }
    }

    Content(
        state = state,
        topRatedContract = viewModel
    )
}

@Composable
private fun Content(
    state: TopRatedUiState,
    topRatedContract: TopRatedContract,
) {
    val screenWidth =
        with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(WindowInsets.navigationBars.asPaddingValues())

    ) {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp),
            title = com.london.presentation.R.string.top_rated.string,
            onBackClick = topRatedContract::onBackClicked
        )

        TabLayout(
            tabs = listOf(
                TabItem(R.string.movies),
                TabItem(R.string.tv_shows),
            ),
            selectedIndex = state.tabSelected,
            onTabSelected = topRatedContract::tabSelected,
            modifier = Modifier.background(NovixTheme.colors.surface)
        )
        if (state.isMovieSelected)
            MovieGenreRow(
                onGenreClick = topRatedContract::movieGenre,
                state = state,
                screenWidth = screenWidth
            )
        else
            TvShowRow(
                onGenreClick = topRatedContract::tvShowGenre,
                state = state,
                screenWidth = screenWidth
            )

        val moviesPagingItems = state.movies.collectAsLazyPagingItems()
        val tvSeriesPagingItems = state.tvSeries.collectAsLazyPagingItems()

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                top = 12.dp, bottom = 16.dp, start = 16.dp, end = 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.background(NovixTheme.colors.surface)
        ) {

            if (state.isMovieSelected) {
                items(moviesPagingItems.itemCount) { index ->
                    val movie = moviesPagingItems[index]
                    movie?.let { movieItem ->
                        HomeCard(
                            imageUrl = movieItem.posterUrl,
                            isSaved = false,
                            onSaveClick = {
                                // TODO
                            },
                            modifier = Modifier.clickable {
                                topRatedContract.onMovieClick(movieItem.id)
                            }
                        )
                    }
                }
            }
            items(tvSeriesPagingItems.itemCount) { index ->
                val tvSeries = tvSeriesPagingItems[index]
                tvSeries?.let { seriesItem ->
                    HomeCard(
                        imageUrl = seriesItem.posterUrl,
                        isSaved = false,
                        onSaveClick = {
                            // TODO
                        },
                        modifier = Modifier.clickable {
                            topRatedContract.onTvShowClick(seriesItem.id)
                        }
                    )
                }
            }
        }
    }

}

@Composable
private fun MovieGenreRow(
    onGenreClick: (MovieGenre) -> Unit,
    state: TopRatedUiState,
    screenWidth: Dp,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier
            .requiredWidth(screenWidth)
            .padding(vertical = 12.dp)
    ) {
        items(MovieGenre.entries.toTypedArray()) { genre ->
            NovixChip(
                text = stringResource(genre.stringResId),
                isSelected = genre == state.selectedMovieGenre,
                onClick = { onGenreClick(genre) })
        }
    }
}

@Composable
private fun TvShowRow(
    onGenreClick: (TvShowGenre) -> Unit,
    state: TopRatedUiState,
    screenWidth: Dp,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier
            .requiredWidth(screenWidth)
            .padding(vertical = 12.dp)
    ) {
        items(TvShowGenre.entries.toTypedArray()) { genre ->
            NovixChip(
                text = stringResource(genre.stringResId),
                isSelected = genre == state.selectedTvShowGenre,
                onClick = { onGenreClick(genre) })
        }
    }
}

@ThemePreviews
@Composable
private fun TopRatedPreview() {
    val mockTopRatedMovie = TopRatedMovie(
        adult = false,
        backdropUrl = "https://example.com/backdrop.jpg",
        id = 1,
        originalLanguage = "en",
        originalTitle = "Original Movie Title",
        overview = "This is a top rated movie overview.",
        popularity = 1523.3,
        posterUrl = "https://example.com/poster.jpg",
        releaseDate = "2024-03-15",
        title = "Top Rated Movie",
        video = false,
        voteAverage = 8.7,
        voteCount = 5400,
        genreIds = listOf(28, 18)
    )
    val mockTopRatedTvSeries = TopRatedTvSeries(
        adult = false,
        backdropUrl = "https://example.com/tv_backdrop.jpg",
        firstAirDate = "2023-09-10",
        genreIds = listOf(10759, 18),
        id = 100,
        name = "Top Rated Series",
        originCountry = listOf("US"),
        originalLanguage = "en",
        originalName = "Original Series Name",
        overview = "This is a top rated TV series overview.",
        popularity = 998.4,
        posterUrl = "https://example.com/tv_poster.jpg",
        voteAverage = 8.4,
        voteCount = 3100
    )
    val mockMoviesPagingData = PagingData.from(
        listOf(
            mockTopRatedMovie,
            mockTopRatedMovie.copy(id = 2, title = "Top Rated Movie 2")
        )
    )

    val mockTvSeriesPagingData = PagingData.from(
        listOf(
            mockTopRatedTvSeries,
            mockTopRatedTvSeries.copy(id = 101, name = "Top Rated Series 2")
        )
    )
    val mockTopRatedUiState = TopRatedUiState(
        movies = flow { emit(mockMoviesPagingData) },
        tvSeries = flow { emit(mockTvSeriesPagingData) },
        isLoading = false,
        errorMessage = null,
        tabSelected = 0,
        selectedMovieGenre = MovieGenre.All,
        selectedTvShowGenre = TvShowGenre.All,
        isMovieSelected = true
    )

    Content(
        state = mockTopRatedUiState,
        topRatedContract = object : TopRatedContract {
            override fun movieGenre(genre: MovieGenre) {
                TODO("Not yet implemented")
            }

            override fun tvShowGenre(genre: TvShowGenre) {
                TODO("Not yet implemented")
            }

            override fun tabSelected(index: Int) {
                TODO("Not yet implemented")
            }

            override fun onBackClicked() {
                TODO("Not yet implemented")
            }

            override fun onMovieClick(id: Int) {
                TODO("Not yet implemented")
            }

            override fun onTvShowClick(id: Int) {
                TODO("Not yet implemented")
            }
        }
    )
}