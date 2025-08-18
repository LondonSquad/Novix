package com.london.presentation.feature.details.actor.info.toptvshowspicks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.presentation.R
import com.london.presentation.shared.DefaultAppTopBar
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.shared.container.MediaLazyVerticalGrid
import com.london.presentation.utils.Listen

@Composable
fun TopTvShowsPicksScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTvShowDetails: (Int) -> Unit,
    viewModel: TopTvShowsPicksViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(initial = null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TopTvShowsPicksEffect.BackNavigation -> onNavigateBack()
            is TopTvShowsPicksEffect.TvShowDetailsNavigation -> onNavigateToTvShowDetails(
                currentEffect.tvShowId
            )
        }
    }

    Content(
        state = state,
        contract = viewModel,
    )
}

@Composable
private fun Content(
    state: TopTvShowsPicksUiState,
    contract: TopTvShowsPicksContract,
) {
    BuildScreen(
        onBack = contract::onBackClick,
        isLoading = state.isLoading,
        isError = state.errorState is ErrorState.NoInternet,
        onRetry = contract::onRetryClick
    ) {
        MediaLazyVerticalGrid(
            items = state.tvShowDetails.mediaItems,
            imageUrl = { it.posterUrl },
            onItemClick = { contract.onTvShowClick(it.id) },
            onSaveClick = { contract.onSaveTvShowClick(it.id) },
            topBar = {
                DefaultAppTopBar(
                    title = stringResource(R.string.top_tv_shows_picks),
                    onBackClick = contract::onBackClick,
                )
            }
        )
    }
}
