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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.R
import com.london.presentation.shared.HomeCard
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.TabItem
import com.london.designsystem.component.TabLayout
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.utils.string
import com.london.presentation.feature.buildscreen.BuildScreen
import com.london.presentation.utils.Listen
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre
import com.london.presentation.utils.gridColmuns
import com.london.presentation.utils.isLoading

@Composable
fun TopRatedScreen(
    viewModel: TopRatedViewModel = hiltViewModel(),
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

    val topRatedMovieFlow = state.movies.collectAsLazyPagingItems()
    val topRatedTvShowFlow = state.tvSeries.collectAsLazyPagingItems()

    BuildScreen(
        isLoading = topRatedTvShowFlow.isLoading() && topRatedMovieFlow.isLoading(),
        isError = topRatedMovieFlow.loadState.refresh is LoadState.Error
                && topRatedTvShowFlow.loadState.refresh is LoadState.Error,
        onBack = viewModel::onBackClicked,
        onRetry = viewModel::onRetry,
    ) {
        Content(
            state = state,
            topRatedContract = viewModel
        )
    }
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
            columns = GridCells.Fixed(gridColmuns()),
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