package com.london.presentation.feature.details.actor.info.toptvshowspicks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.presentation.R
import com.london.presentation.shared.MediaLazyGrid
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.utils.Listen

@Composable
fun TopTvShowsPicksScreen(
    onNavigateBack: () -> Unit,
    onNavigateTvShow: (Int) -> Unit,
    viewModel: TopTvShowsPicksViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(initial = null)

    HandleTvShowsPicksEffects(
        effect = effect,
        onNavigateBack = onNavigateBack,
        onNavigateTvShow = onNavigateTvShow,
    )

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
        MediaLazyGrid(
            title = stringResource(R.string.top_tv_shows_picks),
            items = state.actorTvShowDetails.cast,
            onBack = contract::onBackClick,
            getImageUrl = { it.posterUrl },
            onItemClick = { contract.onTvShowClick(it.id) },
            onSavedClick = { contract.onSaveClick(it.id) },
        )
    }
}

@Composable
private fun HandleTvShowsPicksEffects(
    onNavigateBack: () -> Unit,
    onNavigateTvShow: (Int) -> Unit,
    effect: TopTvShowsPicksEffect?,
) {
    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TopTvShowsPicksEffect.BackNavigation -> onNavigateBack()
            is TopTvShowsPicksEffect.TvShowDetailsNavigation -> onNavigateTvShow(currentEffect.tvShowId)
        }
    }
}
