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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.DefaultTopBar
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.utils.string
import com.london.domain.entity.UpComingMovie
import com.london.presentation.R
import com.london.presentation.feature.home.continuewatching.continueWatchingSection
import com.london.presentation.feature.home.popular.popularSection
import com.london.presentation.feature.home.toprated.topRatedSection
import com.london.presentation.feature.home.trending.trendingSection
import com.london.presentation.feature.home.upcoming.upcomingSection
import com.london.presentation.shared.buildscreen.NetworkErrorScreen
import com.london.presentation.utils.Listen
import com.london.presentation.utils.gridColumns

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
            is HomeScreenEffect.NavigationMovieDetails -> onNavigateToMovieDetails(currentEffect.id)
            is HomeScreenEffect.NavigationTvShowDetails -> onNavigateToTvShowDetails(currentEffect.id)
            is HomeScreenEffect.NavigationTrendingMovie -> onNavigateToTrendingMovies()
            is HomeScreenEffect.NavigationTrendingTvShows -> onNavigateToTrendingTvShows()
            is HomeScreenEffect.NavigationTrendingActor -> onNavigateToTrendingActors()
            is HomeScreenEffect.NavigationTopRated -> onNavigateToTopRated()
            is HomeScreenEffect.NavigationContinueWatching -> onNavigateToContinueWatching()
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
    pagerState: androidx.compose.foundation.pager.PagerState,
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
    pagerState: androidx.compose.foundation.pager.PagerState,
    recentWatchedMedia: List<HomeUiMedia>,
    upcomingMoviesLazyList: LazyPagingItems<UpComingMovie>,
    scrollState: ScrollState,
    homeScreenContract: HomeScreenContract
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        BackgroundGradient(
            screenWidth = screenWidth,
            modifier = Modifier.align(Alignment.TopStart)
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
}

@Composable
private fun BackgroundGradient(
    screenWidth: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(400.dp)
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
            .padding(top = 12.dp, bottom = 8.dp)
    )
}

@Composable
private fun HomeContentGrid(
    lazyGridState: LazyGridState,
    screenWidth: Dp,
    uiState: HomeScreenUiState,
    pagerState: androidx.compose.foundation.pager.PagerState,
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
    val recentWatchedMediaFlow by uiState.recentWatchedMediaFlow.collectAsStateWithLifecycle(emptyList())

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