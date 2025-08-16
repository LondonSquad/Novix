package com.london.presentation.feature.home.continuewatching

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.shared.DefaultAppTopBar
import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.shared.container.MediaGridConfig
import com.london.presentation.shared.container.MediaLazyGridWithTabs
import com.london.presentation.utils.Listen

@Composable
fun ContinueWatchingScreen(
    screenTitle: String,
    onNavigateBack: () -> Unit = {},
    onNaviagteToMovieDetalis: (Int) -> Unit = {},
    onNaviagteToTvShowDetalis: (Int) -> Unit = {},
    viewModel: ContinueWatchingViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen {
        when (it) {
            is ContinueWatchingEffect.NavigateToMovieDetails -> onNaviagteToMovieDetalis(it.id)
            is ContinueWatchingEffect.NavigateToTvShowDetails -> onNaviagteToTvShowDetalis(it.id)
            is ContinueWatchingEffect.NavigateBack -> onNavigateBack()
        }
    }
    BuildScreen(
        onBack = viewModel::onBackClick,
        isLoading = state.isLoading,
        isError = state.error is ErrorState.NoInternet,
        onRetry = viewModel::onRetryCLick,
    ) {
        Content(
            state = state,
            contract = viewModel,
            screenTitle = screenTitle
        )
    }
}

@Composable
fun Content(
    state: ContinueWatchingUiState,
    contract: ContinueWatchingContract,
    screenTitle: String = stringResource(R.string.continue_watch)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
            MediaLazyGridWithTabs(
                items = getCombinedItems(state),
                tabSelected = getSelectedTabIndex(state),
                onTabSelected = contract::onMediaCategoryTabClick,
                onMovieGenreClick = contract::onMovieGenreClick,
                onTvShowGenreClick = contract::onTvShowGenreClick,
                config = MediaGridConfig(
                    showSaveIcon = true,
                    isDarkMode = NovixTheme.isThemeDark,
                    isMovieSelected = state.isMovieSelected,
                    isTvShowSelected = state.isTvSelected,
                    selectedMovieGenre = state.selectedMovieGenre,
                    selectedTvShowGenre = state.selectedTvShowGenre,
                    onNavigateToMovie = contract::onNavigateToMovie,
                    onNavigateToTvShow = contract::onNavigateToTvShow,
                    onSaveClick = { /* TODO: Implement save functionality */ },
                    isItemSaved = { false },
                ),
                topBar = {
                    DefaultAppTopBar(
                        title = screenTitle,
                        onBack = contract::onBackClick
                    )
                },
                isLoading = state.isLoading
            )
        }
    }

@Composable
private fun getCombinedItems(state: ContinueWatchingUiState): List<Any> =
    state.movies.collectAsStateWithLifecycle(emptyList()).value +
            state.tvSeries.collectAsStateWithLifecycle(emptyList()).value

private fun getSelectedTabIndex(state: ContinueWatchingUiState): Int =
    if (state.isMovieSelected) MediaCategory.Movies.ordinal else MediaCategory.TvShows.ordinal
