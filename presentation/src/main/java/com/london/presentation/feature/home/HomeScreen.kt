package com.london.presentation.feature.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.DefaultTopBar
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.utils.string
import com.london.domain.entity.movie.UpComingMovie
import com.london.domain.entity.shared.MediaType
import com.london.presentation.R
import com.london.presentation.feature.home.popular.PopularSection
import com.london.presentation.feature.home.section.ContinueWatchingSection
import com.london.presentation.feature.home.section.ShimmerPopularSection
import com.london.presentation.feature.home.section.TopRatedSection
import com.london.presentation.feature.home.trending.TrendingSection
import com.london.presentation.feature.home.upcoming.UpcomingMovieItem
import com.london.presentation.feature.home.upcoming.UpcomingSectionTitle
import com.london.presentation.feature.home.upcoming.UpcomingStickyHeader
import com.london.presentation.shared.BackgroundGradient
import com.london.presentation.shared.CarousalShimmerEffect
import com.london.presentation.shared.bookmarkSheet.BookmarkBottomSheet
import com.london.presentation.shared.buildscreen.NetworkErrorScreen
import com.london.presentation.utils.Listen
import com.london.presentation.utils.gridColumns
import com.london.presentation.utils.navBarBottomPadding

@Composable
fun HomeScreen(
    onNavigateToTopRated: () -> Unit = {},
    onNavigateToTrendingActors: () -> Unit = {},
    onNavigateToTrendingMovies: () -> Unit = {},
    onNavigateToTrendingTvShows: () -> Unit = {},
    onNavigateToContinueWatching: () -> Unit = {},
    onNavigateToMovieDetails: (movieId: Int) -> Unit = {},
    onNavigateToTvShowDetails: (tvShowId: Int) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is HomeScreenEffect.MovieDetailsNavigation -> onNavigateToMovieDetails(currentEffect.id)
            is HomeScreenEffect.TvShowDetailsNavigation -> onNavigateToTvShowDetails(currentEffect.id)
            is HomeScreenEffect.TrendingMovieNavigation -> onNavigateToTrendingMovies()
            is HomeScreenEffect.TrendingTvShowsNavigation -> onNavigateToTrendingTvShows()
            is HomeScreenEffect.TrendingActorNavigation -> onNavigateToTrendingActors()
            is HomeScreenEffect.TopRatedNavigation -> onNavigateToTopRated()
            is HomeScreenEffect.ContinueWatchingNavigation -> onNavigateToContinueWatching()
        }
    }

    HomeScreenContent(
        uiState = uiState,
        homeScreenContract = viewModel
    )
}

@Composable
private fun HomeScreenContent(
    uiState: HomeScreenUiState,
    homeScreenContract: HomeScreenContract,
) {
    val screenDimensions = rememberScreenDimensions()
    val lazyGridState = rememberLazyGridState()
    val upcomingMoviesLazyList = uiState.upcomingMovies.collectAsLazyPagingItems()
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { if (uiState.popularMediaList.isNotEmpty()) uiState.popularMediaList.size else 3 }
    )

    val scrollState = rememberScrollState(lazyGridState, uiState)
    val recentWatchedMedia by uiState.recentWatchedMediaFlow.collectAsStateWithLifecycle(emptyList())

    HandleScreenState(
        uiState = uiState,
        screenDimensions = screenDimensions,
        lazyGridState = lazyGridState,
        pagerState = pagerState,
        recentWatchedMedia = recentWatchedMedia,
        upcomingMoviesLazyList = upcomingMoviesLazyList,
        scrollState = scrollState,
        homeScreenContract = homeScreenContract
    )
}

@Composable
private fun HandleScreenState(
    uiState: HomeScreenUiState,
    screenDimensions: ScreenDimensions,
    lazyGridState: LazyGridState,
    pagerState: PagerState,
    recentWatchedMedia: List<HomeUiMedia>,
    upcomingMoviesLazyList: LazyPagingItems<UpComingMovie>,
    scrollState: ScrollState,
    homeScreenContract: HomeScreenContract
) {
    when {
        uiState.error != null -> {
            ErrorState(
                onRetry = {
                    homeScreenContract.onRetryClick()
                    upcomingMoviesLazyList.retry()
                }
            )
        }

        else -> {
            HomeScreenLayout(
                modifier = Modifier,
                screenWidth = screenDimensions.width,
                lazyGridState = lazyGridState,
                uiState = uiState,
                pagerState = pagerState,
                recentWatchedMedia = recentWatchedMedia,
                upcomingMoviesLazyList = upcomingMoviesLazyList,
                scrollState = scrollState,
                homeScreenContract = homeScreenContract
            )
        }
    }
}

@Composable
private fun ErrorState(
    onRetry: () -> Unit
) {
    NetworkErrorScreen(
        onRetry = onRetry,
        onBack = null
    )
}

@Composable
private fun HomeScreenLayout(
    modifier: Modifier,
    screenWidth: Dp,
    lazyGridState: LazyGridState,
    uiState: HomeScreenUiState,
    pagerState: PagerState,
    recentWatchedMedia: List<HomeUiMedia>,
    upcomingMoviesLazyList: LazyPagingItems<UpComingMovie>,
    scrollState: ScrollState,
    homeScreenContract: HomeScreenContract
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navBarBottomPadding()
    ) {
        BackgroundGradient(
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(1f)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            HomeTopBar(modifier = Modifier.fillMaxWidth())

            Box(modifier = modifier.fillMaxSize()) {
                HomeContentGrid(
                    lazyGridState = lazyGridState,
                    screenWidth = screenWidth,
                    uiState = uiState,
                    pagerState = pagerState,
                    recentWatchedMedia = recentWatchedMedia,
                    upcomingMoviesLazyList = upcomingMoviesLazyList,
                    scrollState = scrollState,
                    homeScreenContract = homeScreenContract
                )

                if (scrollState.isAtEndOfGrid) {
                    FloatingRetryButton(
                        onRetry = { upcomingMoviesLazyList.retry() },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }

    BookmarkBottomSheet(
        onSheetDismiss = homeScreenContract::onBookmarkSheetDismiss,
        isSheetVisible = uiState.isBookmarkSheetVisible,
        bookmarkedMovieId = uiState.bookmarkedMovieId
    )
}


@Composable
private fun HomeTopBar(modifier: Modifier = Modifier) {
    DefaultTopBar(
        appIconRes = if (NovixTheme.isThemeDark) {
            R.drawable.img_novix_dark
        } else {
            R.drawable.img_novix_light
        },
        appName = R.string.app_name.string,
        appDescription = R.string.app_name_description.string,
        appIconContentDescription = R.string.novix_icon.string,
        modifier = modifier
            .background(NovixTheme.colors.surface)
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(top = 12.dp, bottom = 8.dp)
    )
}

@Composable
private fun HomeContentGrid(
    lazyGridState: LazyGridState,
    screenWidth: Dp,
    uiState: HomeScreenUiState,
    pagerState: PagerState,
    recentWatchedMedia: List<HomeUiMedia>,
    upcomingMoviesLazyList: LazyPagingItems<UpComingMovie>,
    scrollState: ScrollState,
    homeScreenContract: HomeScreenContract
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridColumns()),
        contentPadding = PaddingValues(
            top = 8.dp,
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
        popularSection(
            screenWidth = screenWidth,
            uiState = uiState,
            pagerState = pagerState,
            homeScreenContract = homeScreenContract
        )

        trendingSection(
            isLoading = uiState.isLoading,
            homeScreenContract = homeScreenContract
        )

        topRatedSection(
            screenWidth = screenWidth,
            uiState = uiState,
            homeScreenContract = homeScreenContract
        )

        if (recentWatchedMedia.isNotEmpty()) {
            continueWatchingSection(
                screenWidth = screenWidth,
                recentWatchedMedia = recentWatchedMedia,
                isLoading = uiState.isLoading,
                homeScreenContract = homeScreenContract
            )
        }

        upcomingSection(
            contract = homeScreenContract,
            screenWidth = screenWidth,
            isHeaderStuck = scrollState.isHeaderStuck,
            state = uiState,
            upcomingMoviesLazyList = upcomingMoviesLazyList,
            isLoading = uiState.isLoading
        )
    }
}

private fun LazyGridScope.popularSection(
    screenWidth: Dp,
    uiState: HomeScreenUiState,
    pagerState: PagerState,
    homeScreenContract: HomeScreenContract
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        if (uiState.popularMediaList.isNotEmpty()) {
            PopularSection(
                modifier = Modifier.requiredWidth(screenWidth),
                pagerState = pagerState,
                uiMediaList = uiState.popularMediaList,
                onManageBookmarkClicked = { movieId ->
                    homeScreenContract.onManageBookmarkClicked(movieId)
                },
                onCardClick = { id, mediaType ->
                    when (mediaType) {
                        MediaType.TvShow -> homeScreenContract.onTvShowClick(id)
                        MediaType.Movie -> homeScreenContract.onMovieClick(id)
                    }
                }
            )
        } else {
            ShimmerPopularSection(
                modifier = Modifier.requiredWidth(screenWidth),
                pagerState = pagerState,
            )
        }
    }
}

private fun LazyGridScope.trendingSection(
    isLoading: Boolean,
    homeScreenContract: HomeScreenContract
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        TrendingSection(
            isLoading = isLoading,
            onMoviesClick = homeScreenContract::onTrendingMoviesCardClick,
            onTvShowsClick = homeScreenContract::onTrendingTvShowsCardClick,
            onActorsClick = homeScreenContract::onTrendingActorsCardClick
        )
    }
}

private fun LazyGridScope.topRatedSection(
    screenWidth: Dp,
    uiState: HomeScreenUiState,
    homeScreenContract: HomeScreenContract
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        if (!uiState.isTopRatedLoading) {
            TopRatedSection(
                uiState = uiState,
                homeScreenContract = homeScreenContract,
                modifier = Modifier.requiredWidth(screenWidth)
            )
        } else {
            CarousalShimmerEffect()
        }
    }
}

private fun LazyGridScope.continueWatchingSection(
    screenWidth: Dp,
    recentWatchedMedia: List<HomeUiMedia>,
    isLoading: Boolean,
    homeScreenContract: HomeScreenContract
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        if (!isLoading) {
            ContinueWatchingSection(
                recentWatchedMediaList = recentWatchedMedia,
                homeScreenContract = homeScreenContract,
                modifier = Modifier.requiredWidth(screenWidth)
            )
        } else {
            CarousalShimmerEffect()
        }
    }
}

private fun LazyGridScope.upcomingSection(
    contract: HomeScreenContract,
    isHeaderStuck: Boolean = false,
    screenWidth: Dp,
    state: HomeScreenUiState,
    upcomingMoviesLazyList: LazyPagingItems<UpComingMovie>,
    isLoading: Boolean = false
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        UpcomingSectionTitle(isLoading = isLoading)
    }

    stickyHeader {
        UpcomingStickyHeader(
            isLoading = isLoading,
            isHeaderStuck = isHeaderStuck,
            screenWidth = screenWidth,
            state = state,
            contract = contract
        )
    }

    items(count = upcomingMoviesLazyList.itemCount) { index ->
        val movie = upcomingMoviesLazyList[index]

        UpcomingMovieItem(
            movie = movie,
            isLoading = isLoading,
            onMovieClick = { contract.onMovieClick(movie?.id ?: 0) },
            onManageBookmarkClick = contract::onManageBookmarkClicked
        )
    }
}

@Composable
private fun FloatingRetryButton(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    PrimaryButton(
        text = "",
        hasLabel = false,
        icon = R.drawable.retry,
        hasIcon = true,
        isLoading = false,
        onClick = onRetry,
        enabled = true,
        modifier = modifier
            .offset(y = (-8).dp)
            .width(52.dp)
    )
}

@Composable
private fun rememberScreenDimensions(): ScreenDimensions {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current

    return remember(density, windowInfo) {
        ScreenDimensions(
            width = with(density) { windowInfo.containerSize.width.toDp() }
        )
    }
}

@Composable
private fun rememberLazyGridState(): LazyGridState {
    return rememberSaveable(saver = LazyGridState.Saver) {
        LazyGridState()
    }
}

@Composable
private fun rememberScrollState(
    lazyGridState: LazyGridState,
    uiState: HomeScreenUiState
): ScrollState {
    val recentWatchedMediaFlow by uiState.recentWatchedMediaFlow.collectAsStateWithLifecycle(
        emptyList()
    )

    val isAtEndOfGrid by remember {
        derivedStateOf {
            val lastVisibleItem = lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = lazyGridState.layoutInfo.totalItemsCount
            lastVisibleItem?.index == totalItems - 1
        }
    }

    val isHeaderStuck by remember {
        derivedStateOf {
            val itemsBeforeStickyHeader = if (recentWatchedMediaFlow.isNotEmpty()) 5 else 4
            val scrollThreshold = 42
            lazyGridState.firstVisibleItemIndex > itemsBeforeStickyHeader ||
                (lazyGridState.firstVisibleItemIndex == itemsBeforeStickyHeader &&
                    lazyGridState.firstVisibleItemScrollOffset > scrollThreshold)
        }
    }

    return ScrollState(
        isAtEndOfGrid = isAtEndOfGrid,
        isHeaderStuck = isHeaderStuck
    )
}

private data class ScreenDimensions(
    val width: Dp
)

private data class ScrollState(
    val isAtEndOfGrid: Boolean,
    val isHeaderStuck: Boolean
)
