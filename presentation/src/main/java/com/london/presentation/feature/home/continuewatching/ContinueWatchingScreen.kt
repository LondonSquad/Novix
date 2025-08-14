package com.london.presentation.feature.home.continuewatching

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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.TabLayout
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.presentation.R
import com.london.presentation.feature.home.HomeScreenContract
import com.london.presentation.feature.home.HomeUiMedia
import com.london.presentation.feature.home.section.ContinueWatchingSection
import com.london.presentation.shared.CarousalShimmerEffect
import com.london.presentation.shared.EmptyGenreLayout
import com.london.presentation.shared.HomeCard
import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.utils.Listen
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre
import com.london.presentation.utils.gridColumns

fun LazyGridScope.continueWatchingSection(
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

@Composable
private fun ContinueWatchingScreen(
    screenTitle: String,
    onBackClick: () -> Unit = {},
    onMovieClick: (Int) -> Unit = {},
    onTvShowClick: (Int) -> Unit = {},
    viewModel: ContinueWatchingViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen {
        when (it) {
            is ContinueWatchingEffect.NavigateToMovieDetails -> onMovieClick(it.id)
            is ContinueWatchingEffect.NavigateToTvShowDetails -> onTvShowClick(it.id)
            is ContinueWatchingEffect.NavigateBack -> onBackClick()
        }
    }

    Content(
        state = state,
        continueWatchingContract = viewModel,
        screenTitle = screenTitle
    )
}

@Composable
fun Content(
    state: ContinueWatchingUiState = ContinueWatchingUiState(),
    continueWatchingContract: ContinueWatchingContract = defaultContinueWatchingContract(),
    screenTitle: String = stringResource(R.string.continue_watch)
) {
    val screenWidth = with(LocalDensity.current) {
        LocalWindowInfo.current.containerSize.width.toDp()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        BuildScreen(
            onBack = continueWatchingContract::onBack,
            isLoading = state.isLoading,
            isError = state.error is ErrorState.NoInternet,
            onRetry = continueWatchingContract::onRetry,
        ) {
            TopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp),
                title = screenTitle,
                onBackClick = continueWatchingContract::onBack
            )

            MediaCategoryTabs(
                selectedCategory = state.selectedMediaCategory,
                onTabSelected = continueWatchingContract::onMediaCategoryTabSelected
            )

            GenreFilterRow(
                state = state,
                continueWatchingContract = continueWatchingContract,
                screenWidth = screenWidth
            )

            MediaContentGrid(
                state = state,
                continueWatchingContract = continueWatchingContract
            )
        }
    }
}

@Composable
private fun MediaCategoryTabs(
    selectedCategory: MediaCategory,
    onTabSelected: (MediaCategory) -> Unit
) {
    TabLayout(
        tabs = listOf(MediaCategory.Movies, MediaCategory.TvShows),
        selectedTab = selectedCategory,
        onTabSelected = onTabSelected,
        modifier = Modifier.background(NovixTheme.colors.surface)
    )
}

@Composable
private fun GenreFilterRow(
    state: ContinueWatchingUiState,
    continueWatchingContract: ContinueWatchingContract,
    screenWidth: Dp
) {
    when {
        state.isMovieSelected -> MovieGenreRow(
            onGenreClick = continueWatchingContract::onMovieGenreChanged,
            state = state,
            screenWidth = screenWidth
        )

        state.isTvSelected -> TvShowRow(
            onGenreClick = continueWatchingContract::onTvShowGenreChanged,
            state = state,
            screenWidth = screenWidth
        )
    }
}

@Composable
private fun MediaContentGrid(
    state: ContinueWatchingUiState,
    continueWatchingContract: ContinueWatchingContract
) {
    val movies by state.movies.collectAsStateWithLifecycle(emptyList())
    val tvSeries by state.tvSeries.collectAsStateWithLifecycle(emptyList())

    val isEmpty = (state.isMovieSelected && movies.isEmpty()) ||
            (state.isTvSelected && tvSeries.isEmpty())

    if (isEmpty) {
        EmptyGenreLayout()
    } else {
        MediaGrid(
            state = state,
            movies = movies,
            tvSeries = tvSeries,
            continueWatchingContract = continueWatchingContract
        )
    }
}

@Composable
private fun MediaGrid(
    state: ContinueWatchingUiState,
    movies: List<Movie>,
    tvSeries: List<TvShow>,
    continueWatchingContract: ContinueWatchingContract
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridColumns()),
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
            state.isMovieSelected -> {
                items(movies) { movie ->
                    MovieCard(
                        movie = movie,
                        onMovieClick = continueWatchingContract::onNavigateToMovie
                    )
                }
            }

            else -> {
                items(tvSeries) { series ->
                    TvSeriesCard(
                        series = series,
                        onSeriesClick = continueWatchingContract::onNavigateToTvShow,
                        isDarkMode = NovixTheme.isThemeDark
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieCard(
    movie: Movie,
    onMovieClick: (Int) -> Unit
) {
    HomeCard(
        imageUrl = movie.posterUrl,
        isSaved = false,
        onSaveClick = { /* TODO */ },
        modifier = Modifier.clickable { onMovieClick(movie.id) },
        isDarkMode = NovixTheme.isThemeDark
    )
}

@Composable
private fun TvSeriesCard(
    series: TvShow,
    onSeriesClick: (Int) -> Unit,
    isDarkMode: Boolean
) {
    HomeCard(
        imageUrl = series.posterPicture,
        isSaved = false,
        onSaveClick = { /* TODO */ },
        modifier = Modifier.clickable { onSeriesClick(series.id) },
        isDarkMode = isDarkMode
    )
}

@Composable
private fun MovieGenreRow(
    onGenreClick: (MovieGenre) -> Unit,
    state: ContinueWatchingUiState,
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
    state: ContinueWatchingUiState,
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