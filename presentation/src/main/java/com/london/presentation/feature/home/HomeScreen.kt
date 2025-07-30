package com.london.presentation.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.DefaultTopBar
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.Movie
import com.london.presentation.R
import com.london.presentation.feature.base.ErrorState
import com.london.presentation.feature.buildscreen.LoadingScreen
import com.london.presentation.feature.buildscreen.NetworkErrorScreen
import com.london.presentation.shared.GenresSection
import com.london.presentation.utils.Listen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    onNavigateMovie: (movieId: Int) -> Unit = {},
    onNavigateTvShow: (tvShowId: Int) -> Unit = {},
    onNavigateTopRated: () -> Unit = {},
    onNavigateTrendingMovies: () -> Unit = {},
    onNavigateTrendingTvShows: () -> Unit = {},
    onNavigateTrendingActors: () -> Unit = {},
    onNavigateContinueWatching: () -> Unit = {},
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is HomeScreenEffect.NavigationMovieDetails -> onNavigateMovie(currentEffect.id)
            is HomeScreenEffect.NavigationTvShowDetails -> onNavigateTvShow(currentEffect.id)
            is HomeScreenEffect.NavigationTrendingMovie -> onNavigateTrendingMovies()
            is HomeScreenEffect.NavigationTrendingTvShows -> onNavigateTrendingTvShows()
            is HomeScreenEffect.NavigationTrendingActor -> onNavigateTrendingActors()
            is HomeScreenEffect.NavigationTopRated -> onNavigateTopRated()
            is HomeScreenEffect.NavigationContinueWatching -> onNavigateContinueWatching()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchRecentWatchedMedia()
    }

    val lazyGridState = rememberSaveable(
        saver = LazyGridState.Saver,
    ) {
        LazyGridState()
    }

    when {
        uiState.isLoading -> LoadingScreen()
        uiState.error == ErrorState.NoInternet -> NetworkErrorScreen()
        else -> {
            val screenWidth =
                with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(WindowInsets.statusBars.asPaddingValues())
            ) {

                Box(
                    modifier = Modifier
                        .size(400.dp)
                        .align(Alignment.TopStart)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    NovixTheme.colors.primary.copy(alpha = 0.09f),
                                    Color.Transparent
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(screenWidth.value, 400f)
                            )
                        )
                )
                Column(modifier = Modifier.fillMaxSize()) {
                    DefaultTopBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(NovixTheme.colors.surface)

                    )


                    Content(
                        homeScreenContract = viewModel,
                        uiState = uiState,
                        modifier = Modifier.weight(1f),
                        lazyGridState = lazyGridState,
                        screenWidth = screenWidth
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    uiState: HomeScreenUiState,
    lazyGridState: LazyGridState,
    screenWidth: Dp,
    modifier: Modifier = Modifier,
    homeScreenContract: HomeScreenContract = defaultHomeScreenContract(),
) {
    val upcomingMoviesLazyList = uiState.upcomingMovies.collectAsLazyPagingItems()
    val totalPopularItems = uiState.popularMovies.size + uiState.popularTvShows.size
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { totalPopularItems })

    val isAtEndOfGrid by remember {
        derivedStateOf {
            val lastVisibleItem = lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = lazyGridState.layoutInfo.totalItemsCount
            lastVisibleItem?.index == totalItems - 1
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 158.dp),
            contentPadding = PaddingValues(
                top = 12.dp,
                bottom = 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            state = lazyGridState,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .background(color = NovixTheme.colors.surface)
                .fillMaxSize()
        ) {

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
                        modifier = Modifier
                            .requiredWidth(screenWidth),
                        pagerState = pagerState,
                        images = popularCardImages,
                        onSaveClick = {/*TODO*/ },
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
                TrendingSection(
                    modifier = Modifier.padding(top = 8.dp),
                    onMoviesClick = homeScreenContract::onTrendingMoviesCardClicked,
                    onTvShowsClick = homeScreenContract::onTrendingTvShowsCardClicked,
                    onActorsClick = homeScreenContract::onTrendingActorsCardClicked
                )
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                TopRatedSection(
                    uiState = uiState,
                    homeScreenContract = homeScreenContract,
                    modifier = Modifier.requiredWidth(screenWidth)
                )
            }

            if (uiState.recentWatchedMediaList.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    ContinueWatchingSection(
                        uiState = uiState,
                        homeScreenContract = homeScreenContract,
                        modifier = Modifier.requiredWidth(screenWidth)
                    )
                }
            }

            upComingSection(
                contract = homeScreenContract,
                screenWidth = screenWidth,
                state = uiState,
                upcomingMoviesLazyList
            )
        }

        if (isAtEndOfGrid)
            PrimaryButton(
                text = "",
                hasLabel = false,
                icon = R.drawable.retry,
                hasIcon = true,
                isLoading = false,
                onClick = {
                    upcomingMoviesLazyList.retry()
                },
                enabled = true,
                modifier = Modifier
                    .offset(y = (-8).dp)
                    .align(Alignment.BottomCenter)
                    .width(52.dp)
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
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }

    stickyHeader {
        GenresSection(
            genres = state.movieGenres,
            selectedGenreId = state.selectedMovieGenre.id,
            screenWidth = screenWidth,
            onGenreClick = contract::onMovieGenreSelect,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .background(NovixTheme.colors.surface),
            getGenreId = { it.id },
            getGenreName = { stringResource(it.stringResId) }
        )
    }

    items(count = upcomingMoviesLazyList.itemCount) { index ->
        val movie = upcomingMoviesLazyList[index]

        if (movie != null)
            HomeCard(
                imageUrl = movie.posterPicture,
                isSaved = false,
                onSaveClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clipToBounds()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { contract.onMovieClick(movie.id) }
            )
    }
}

