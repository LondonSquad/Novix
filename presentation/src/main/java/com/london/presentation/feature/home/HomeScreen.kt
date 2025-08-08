package com.london.presentation.feature.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.DefaultTopBar
import com.london.designsystem.component.Text
import com.london.designsystem.component.button.PrimaryButton
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.utils.shimmerEffect
import com.london.designsystem.utils.string
import com.london.domain.entity.UpComingMovie
import com.london.domain.entity.recent.MediaType
import com.london.presentation.R
import com.london.presentation.feature.home.popular.PopularSection
import com.london.presentation.feature.home.section.ContinueWatchingSection
import com.london.presentation.feature.home.section.ShimmerPopularSection
import com.london.presentation.feature.home.section.TopRatedSection
import com.london.presentation.feature.home.trending.TrendingSection
import com.london.presentation.shared.CarousalShimmerEffect
import com.london.presentation.shared.GenresSection
import com.london.presentation.shared.HomeCard
import com.london.presentation.shared.buildscreen.NetworkErrorScreen
import com.london.presentation.utils.Listen
import com.london.presentation.utils.gridColmuns

@Composable
fun HomeScreen(
    onNavigateMovie: (movieId: Int) -> Unit = {},
    onNavigateTvShow: (tvShowId: Int) -> Unit = {},
    onNavigateTopRated: () -> Unit = {},
    onNavigateTrendingMovies: () -> Unit = {},
    onNavigateTrendingTvShows: () -> Unit = {},
    onNavigateTrendingActors: () -> Unit = {},
    onNavigateContinueWatching: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
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

    Content(
        uiState = uiState,
        homeScreenContract = viewModel
    )

}

@Composable
private fun Content(
    uiState: HomeScreenUiState,
    modifier: Modifier = Modifier,
    homeScreenContract: HomeScreenContract = defaultHomeScreenContract(),
) {
    val lazyGridState = rememberSaveable(
        saver = LazyGridState.Saver,
    ) {
        LazyGridState()
    }

    val screenWidth =
        with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }

    val upcomingMoviesLazyList = uiState.upcomingMovies.collectAsLazyPagingItems()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = {
        if (uiState.popularMediaList.isNotEmpty()) uiState.popularMediaList.size else 3
    })
    val isAtEndOfGrid by remember {
        derivedStateOf {
            val lastVisibleItem = lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItems = lazyGridState.layoutInfo.totalItemsCount
            lastVisibleItem?.index == totalItems - 1
        }
    }

    val isLoading = uiState.isLoading
    val recentWatchedMediaFlow by uiState.recentWatchedMediaFlow.collectAsStateWithLifecycle(
        emptyList()
    )

    val isHeaderStuck by remember {
        derivedStateOf {
            val itemsBeforeStickyHeader = if (recentWatchedMediaFlow.isNotEmpty()) 5 else 4
            val scrollThreshold = 42
            lazyGridState.firstVisibleItemIndex > itemsBeforeStickyHeader ||
                    (lazyGridState.firstVisibleItemIndex == itemsBeforeStickyHeader &&
                            lazyGridState.firstVisibleItemScrollOffset > scrollThreshold)
        }
    }



    when {
        uiState.error != null -> NetworkErrorScreen(
            onRetry = {
                homeScreenContract.onRetry()
                upcomingMoviesLazyList.retry()
            },
            onBack = null
        )

        else ->
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    DefaultTopBar(
                        appIconRes = if (NovixTheme.isThemeDark) {
                            R.drawable.img_novix_dark
                        } else {
                            R.drawable.img_novix_light
                        },
                        appName = R.string.app_name.string,
                        appDescription = R.string.app_name_description.string,
                        appIconContentDescription = R.string.novix_icon.string,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(NovixTheme.colors.surface)
                            .padding(top = 12.dp, bottom = 8.dp)
                    )

                    Box(modifier = modifier.fillMaxSize()) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(gridColmuns()),
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

                            item(span = { GridItemSpan(maxLineSpan) }) {
                                if (uiState.popularMediaList.isNotEmpty()) {
                                    PopularSection(
                                        modifier = Modifier.requiredWidth(screenWidth),
                                        pagerState = pagerState,
                                        uiMediaList = uiState.popularMediaList,
                                        onSaveClick = {/*TODO*/ },
                                        onCardClick = { id, mediaType ->
                                            when (mediaType) {
                                                MediaType.TvShow -> homeScreenContract.onTvShowClick(
                                                    id
                                                )

                                                MediaType.Movie -> homeScreenContract.onMovieClick(
                                                    id
                                                )
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

                            item(span = { GridItemSpan(maxLineSpan) }) {
                                TrendingSection(
                                    isLoading = isLoading,
                                    onMoviesClick = homeScreenContract::onTrendingMoviesCardClicked,
                                    onTvShowsClick = homeScreenContract::onTrendingTvShowsCardClicked,
                                    onActorsClick = homeScreenContract::onTrendingActorsCardClicked
                                )
                            }

                            item(span = { GridItemSpan(maxLineSpan) }) {
                                if (!uiState.isTopRatedLoading)
                                    TopRatedSection(
                                        uiState = uiState,
                                        homeScreenContract = homeScreenContract,
                                        modifier = Modifier.requiredWidth(screenWidth)
                                    )
                                else
                                    CarousalShimmerEffect()
                            }

                            if (recentWatchedMediaFlow.isNotEmpty()) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    if (!isLoading)
                                        ContinueWatchingSection(
                                            recentWatchedMediaList = recentWatchedMediaFlow,
                                            homeScreenContract = homeScreenContract,
                                            modifier = Modifier.requiredWidth(screenWidth)
                                        )
                                    else CarousalShimmerEffect()
                                }
                            }

                            upComingSection(
                                contract = homeScreenContract,
                                screenWidth = screenWidth,
                                isHeaderStuck = isHeaderStuck,
                                state = uiState,
                                upcomingMoviesLazyList = upcomingMoviesLazyList,
                                isLoading = isLoading
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
            }

    }

}

private fun LazyGridScope.upComingSection(
    contract: HomeScreenContract,
    isHeaderStuck: Boolean = false,
    screenWidth: Dp,
    state: HomeScreenUiState,
    upcomingMoviesLazyList: LazyPagingItems<UpComingMovie>,
    isLoading: Boolean = false
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        if (!isLoading)
            Text(
                text = stringResource(R.string.upcoming),
                style = NovixTheme.typography.headline.small,
                color = NovixTheme.colors.title,
            )
        else
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .padding(bottom = 4.dp)
                    .wrapContentWidth()
                    .shimmerEffect()
            )
    }

    stickyHeader {
        val animatedPadding by animateDpAsState(
            targetValue = if (isHeaderStuck) 8.dp else 0.dp,
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            ),
            label = "header_padding"
        )

        GenresSection(
            isLoading = isLoading,
            genres = state.movieGenres,
            selectedGenreId = state.selectedMovieGenre.id,
            screenWidth = screenWidth,
            onGenreClick = contract::onMovieGenreSelect,
            modifier = Modifier
                .background(NovixTheme.colors.surface)
                .padding(bottom = animatedPadding),
            getGenreId = { it.id },
            getGenreName = { stringResource(it.stringResId) }
        )
    }

    items(count = upcomingMoviesLazyList.itemCount) { index ->
        val movie = upcomingMoviesLazyList[index]

        when {
            isLoading || movie == null -> {
                ShimmerMovieCard()
            }
            else -> {
                HomeCard(
                    imageUrl = movie.imageUrl,
                    isSaved = false,
                    onSaveClick = { /*TODO*/ },
                    modifier = Modifier
                        .clipToBounds()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { contract.onMovieClick(movie.id) }
                )
            }
        }
    }
}

@Composable
private fun ShimmerMovieCard() {
    Box(
        modifier = Modifier
            .height(240.dp)
            .clip(RoundedCornerShape(12.dp))
            .shimmerEffect()
    )
}