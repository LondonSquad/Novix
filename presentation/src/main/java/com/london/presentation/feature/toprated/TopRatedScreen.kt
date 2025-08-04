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
import com.london.designsystem.component.HomeCard
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
        MediaGridWithTabsPaging(
            screenTitle = R.string.top_rating,
            moviesPagingItems = topRatedMovieFlow,
            tvShowsPagingItems = topRatedTvShowFlow,
            tabSelected = state.tabSelected,
            selectedMovieGenre = state.selectedMovieGenre,
            selectedTvShowGenre = state.selectedTvShowGenre,
            isMovieSelected = state.isMovieSelected,
            isTvSelected = !state.isMovieSelected,
            onBackClick = viewModel::onBackClicked,
            onTabSelected = viewModel::tabSelected,
            onMovieGenreClick = viewModel::movieGenre,
            onTvShowGenreClick = viewModel::tvShowGenre,
            onMovieClick = viewModel::onMovieClick,
            onTvShowClick = viewModel::onTvShowClick,
            isLoading = isLoading,
            emptyTitle = stringResource(R.string.no_content_found),
            emptyImage = R.drawable.img_no_reviews
        )
    }
}