package com.london.presentation.feature.details.actor.info.topmoviespicks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.presentation.R
import com.london.presentation.shared.DefaultAppTopBar
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.bookmarkSheet.BookmarkBottomSheet
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.shared.container.MediaLazyVerticalGrid
import com.london.presentation.utils.Listen

@Composable
fun TopMoviesPicksScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMovieDetails: (Int) -> Unit,
    viewModel: TopMoviesPicksViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TopMoviesPicksEffect.BackNavigation -> onNavigateBack()
            is TopMoviesPicksEffect.MovieDetailsNavigation -> onNavigateToMovieDetails(currentEffect.movieId)
        }
    }

    Content(
        state = state,
        contract = viewModel,
    )

}

@Composable
private fun Content(
    state: TopMoviesPicksUiState,
    contract: TopMoviesPicksContract,
) {
    BuildScreen(
        onBack = contract::onBackClick,
        isLoading = state.isLoading,
        isError = state.errorState is ErrorState.NoInternet,
        onRetry = contract::onRetryClick,
    ) {
        MediaLazyVerticalGrid(
            items = state.movieDetails.mediaItems,
            imageUrl = { it.posterUrl },
            hasSaveIcon = true,
            onItemClick = { contract.onMovieClick(it.id) },
            onSaveClick = { contract.onManageBookmarkClicked(it.id) },
            topBar = {
                DefaultAppTopBar(
                    title = stringResource(R.string.top_movies_picks),
                    onBackClick = contract::onBackClick
                )
            }
        )

        BookmarkBottomSheet(
            onSheetDismiss = contract::onBookmarkSheetDismiss,
            isSheetVisible = state.isBookmarkSheetVisible,
            bookmarkedMovieId = state.bookmarkedMovieId
        )
    }
}
