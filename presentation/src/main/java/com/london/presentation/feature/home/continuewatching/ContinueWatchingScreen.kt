package com.london.presentation.feature.home.continuewatching

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
    onNavigateToMovieDetails: (Int) -> Unit = {},
    onNavigateToTvShowDetails: (Int) -> Unit = {},
    viewModel: ContinueWatchingViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is ContinueWatchingEffect.NavigateToMovieDetails ->
                onNavigateToMovieDetails(currentEffect.id)

            is ContinueWatchingEffect.NavigateToTvShowDetails ->
                onNavigateToTvShowDetails(currentEffect.id)

            is ContinueWatchingEffect.NavigateBack ->
                onNavigateBack()
        }
    }

    BuildScreen(
        onBack = viewModel::onBackClick,
        isLoading = state.isLoading,
        isError = state.error is ErrorState.NoInternet,
        onRetry = viewModel::onRetryClick,
    ) {
        Content(
            state = state,
            contract = viewModel,
            screenTitle = screenTitle
        )
    }
}

@Composable
private fun Content(
    state: ContinueWatchingUiState,
    contract: ContinueWatchingContract,
    screenTitle: String = stringResource(R.string.continue_watch)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
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
                selectedMovieGenre = state.selectedMovieGenre,
                selectedTvShowGenre = state.selectedTvShowGenre,
                isMovieSelected = MediaCategory.Movies == state.selectedMediaCategory,
                isTvShowSelected = MediaCategory.TvShows == state.selectedMediaCategory,
                onNavigateToMovie = contract::onNavigateToMovieClick,
                onNavigateToTvShow = contract::onNavigateToTvShowClick,
                onSaveClick = { /* TODO: Implement save functionality */ },
                isItemSaved = { false },
                rate = null
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
private fun getCombinedItems(state: ContinueWatchingUiState): List<Any> {
    return state.movies.collectAsStateWithLifecycle(emptyList()).value +
        state.tvSeries.collectAsStateWithLifecycle(emptyList()).value
}

private fun getSelectedTabIndex(state: ContinueWatchingUiState): Int {
    if (state.selectedMediaCategory == MediaCategory.Movies) return MediaCategory.Movies.ordinal
    return MediaCategory.TvShows.ordinal
}
