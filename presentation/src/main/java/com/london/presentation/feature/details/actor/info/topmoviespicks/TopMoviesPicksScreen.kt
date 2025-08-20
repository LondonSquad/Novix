package com.london.presentation.feature.details.actor.info.topmoviespicks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.presentation.R
import com.london.presentation.shared.BackgroundGradient
import com.london.presentation.shared.MediaLazyGrid
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.bookmarkSheet.BookmarkBottomSheet
import com.london.presentation.shared.buildscreen.BuildScreen
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

        Box(
            modifier = Modifier.fillMaxSize()
        ){

            BackgroundGradient(
                modifier = Modifier.align(Alignment.TopStart).zIndex(1f)
            )
            MediaLazyGrid(
                title = stringResource(R.string.top_movies_picks),
                items = state.movieDetails.mediaItems,
                onBack = contract::onBackClick,
                getImageUrl = { it.posterUrl },
                onItemClick = { contract.onMovieClick(it.id) },
                onSavedClick = { contract.onManageBookmarkClicked(it.id) },
                hasSaveIcon = true
            )

            BookmarkBottomSheet(
                onSheetDismiss = contract::onBookmarkSheetDismiss,
                isSheetVisible = state.isBookmarkSheetVisible,
                bookmarkedMovieId = state.bookmarkedMovieId
            )
        }
    }
}
