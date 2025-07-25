package com.london.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.DefaultTopBar
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.Movie
import com.london.presentation.R
import com.london.presentation.screen.LoadingScreen
import com.london.presentation.screen.NetworkErrorScreen
import com.london.presentation.screen.base.ErrorState
import com.london.presentation.utils.Listen
import com.london.presentation.utils.MovieGenre
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    onMovieClick: (movieId: Int) -> Unit = {},
    onTvShowClick: (tvShowId: Int) -> Unit = {},
    viewModel: HomeViewModel = koinViewModel()
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is HomeScreenEffect.NavigationMovieDetails -> onMovieClick(currentEffect.id)
            is HomeScreenEffect.NavigationTvShowDetails -> onTvShowClick(currentEffect.id)
        }
    }

    val lazyGridState = rememberSaveable(
        saver = LazyGridState.Saver,
    ) {
        LazyGridState()
    }
    when {
        uiState.isLoading -> LoadingScreen()
        uiState.error == ErrorState.NoInternet -> NetworkErrorScreen()
        else -> Content(
            homeScreenContract = viewModel,
            uiState = uiState,
            modifier = Modifier
                .fillMaxSize(),
            lazyGridState = lazyGridState
        )
    }
}

@Composable
private fun Content(
    homeScreenContract: HomeScreenContract,
    uiState: HomeScreenUiState,
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState
) {

    val density = LocalDensity.current
    val screenWidth = with(density) {
        LocalConfiguration.current.screenWidthDp.dp
    }
    val upcomingMoviesLazyList = uiState.upcomingMovies.collectAsLazyPagingItems()

    val totalPopularItems = uiState.popularMovies.size + uiState.popularTvShows.size

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { totalPopularItems })


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            top = 12.dp,
            bottom = 16.dp,
            start = 16.dp,
            end = 16.dp
        ),
        state = lazyGridState,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .background(color = NovixTheme.colors.surface)
            .padding(top = 16.dp)
    ) {

        stickyHeader {
            DefaultTopBar(
                modifier = Modifier
                    .requiredWidth(screenWidth)
                    .background(NovixTheme.colors.surface)
                    .padding(top = 12.dp)
            )
        }

        if (totalPopularItems > 0) {
            val moviesCount = uiState.popularMovies.size
            val currentPage = pagerState.currentPage

            val popularCardImages = uiState.popularMovies.map { it.posterUrl } +
                    uiState.popularTvShows.map { it.posterUrl }

            val popularCardRating = uiState.popularMovies.map { it.rating } +
                    uiState.popularTvShows.map { it.rating }

            val popularCardTitle = uiState.popularMovies.map { it.title } +
                    uiState.popularTvShows.map { it.name }

            item(span = { GridItemSpan(maxLineSpan) }) {
                PopularSection(
                    modifier = modifier
                        .requiredWidth(screenWidth),
                    pagerState = pagerState,
                    images = popularCardImages,
                    onSaveClick = {/*TODO: SAVE FUNCTIONALITY IS NOT IMPLEMENTED.*/ },
                    cardRating = popularCardRating[currentPage].toString(),
                    cardTitle = popularCardTitle[currentPage],
                    onCardClick = {
                        if (currentPage < moviesCount) {
                            homeScreenContract.onMovieClick(
                                uiState.popularMovies[currentPage].id
                            )
                        } else {
                            val tvShowIndex = currentPage - moviesCount
                            if (tvShowIndex < uiState.popularTvShows.size) {
                                homeScreenContract.onTvShowClick(
                                    uiState.popularTvShows[tvShowIndex].id
                                )
                            }
                        }
                    }
                )
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            TrendingSection()
        }

        upComingSection(
            contract = homeScreenContract,
            screenWidth = screenWidth,
            state = uiState,
            upcomingMoviesLazyList
        )
    }
}

private fun LazyGridScope.upComingSection(
    contract: HomeScreenContract,
    screenWidth: Dp,
    state: HomeScreenUiState,
    upcomingMoviesLazyList: LazyPagingItems<Movie>

) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Text(
            text = stringResource(R.string.upcoming),
            style = NovixTheme.typography.headline.small,
            color = NovixTheme.colors.title,
        )
    }

    item(span = { GridItemSpan(maxLineSpan) }) {
        GenresSection(
            screenWidth = screenWidth,
            state = state,
            contract = contract
        )
    }

    items(count = upcomingMoviesLazyList.itemCount) { index ->
        val movie = upcomingMoviesLazyList[index]

        if (movie != null)
            HomeCard(
                imageUrl = movie.posterPicture,
                isSaved = false,
                onSaveClick = { /*TODO: SAVE FUNCTIONALITY IS NOT IMPLEMENTED .*/ },
                modifier = Modifier.clickable { contract.onMovieClick(movie.id) })
    }
}

@Composable
private fun GenresSection(
    contract: HomeScreenContract,
    screenWidth: Dp,
    state: HomeScreenUiState
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.requiredWidth(screenWidth)
    ) {
        items(MovieGenre.entries.toTypedArray()) { genre ->
            NovixChip(
                text = genre.name,
                isSelected = (genre == state.selectedGenre),
                onClick = { contract.onGenreSelect(genre) }
            )
        }
    }
}
