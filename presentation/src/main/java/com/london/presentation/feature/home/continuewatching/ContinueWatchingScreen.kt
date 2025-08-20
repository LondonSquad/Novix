package com.london.presentation.feature.home.continuewatching

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.movie.Movie
import com.london.presentation.R
import com.london.presentation.shared.BackgroundGradient
import com.london.presentation.shared.MediaCategory
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.bookmarkSheet.BookmarkBottomSheet
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.shared.container.MediaGridConfig
import com.london.presentation.shared.container.MediaLazyGridWithTabs
import com.london.presentation.utils.Listen
import com.london.presentation.utils.detailsTopBar

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
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        BackgroundGradient(
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(1f)
        )

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
                    showSaveIcon = state.selectedMediaCategory == MediaCategory.Movies,
                    isDarkMode = NovixTheme.isThemeDark,
                    selectedMovieGenre = state.selectedMovieGenre,
                    selectedTvShowGenre = state.selectedTvShowGenre,
                    isMovieSelected = MediaCategory.Movies == state.selectedMediaCategory,
                    isTvShowSelected = MediaCategory.TvShows == state.selectedMediaCategory,
                    onNavigateToMovie = contract::onNavigateToMovieClick,
                    onNavigateToTvShow = contract::onNavigateToTvShowClick,
                    onSaveClick = { if (it is Movie) contract.onManageBookmarkClicked(it.id) },
                    isItemSaved = { false }
                ),
                topBar = {
                    TopBar(
                        modifier = Modifier.detailsTopBar(1f),
                        title = screenTitle,
                        onBackClick = contract::onBackClick
                    )
                },
                isLoading = state.isLoading
            )

            BookmarkBottomSheet(
                onSheetDismiss = contract::onBookmarkSheetDismiss,
                isSheetVisible = state.isBookmarkSheetVisible,
                bookmarkedMovieId = state.bookmarkedMovieId
            )
        }
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
