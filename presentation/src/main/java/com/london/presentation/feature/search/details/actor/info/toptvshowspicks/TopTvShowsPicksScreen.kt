package com.london.presentation.feature.search.details.actor.info.toptvshowspicks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
    onNavigateTvShow: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: TopTvShowsPicksViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(initial = null)

    HandleTvShowsPicksEffects(
        effect = effect,
        onNavigateTvShow = onNavigateTvShow,
        onNavigateBack = onNavigateBack
    )

    TopTvShowsPicksContent(
        state = state,
        contract = viewModel,
    )
}

@Composable
private fun TopTvShowsPicksContent(
    state: TopTvShowsPicksUiState,
    contract: TopTvShowsPicksContract,
    modifier: Modifier = Modifier,
) {
    BuildScreen(
        onBack = contract::onBack,
        isLoading = state.isLoading,
        isError = state.errorState is ErrorState.NoInternet,
        onRetry = contract::onRetry
    ) {
        MediaLazyGrid(
            title = stringResource(R.string.top_tv_shows_picks),
            items = state.tvShowDetails.cast,
            onBack = contract::onBack,
            getImageUrl = { it.posterUrl },
            onItemClick = { contract.onTvShowClicked(it.id) },
            onSavedClick = { contract.onSaveTvShow(it.id) },
            modifier = modifier
        )
    }
}

@Composable
private fun HandleTvShowsPicksEffects(
    effect: TopTvShowsPicksEffect?,
    onNavigateTvShow: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TopTvShowsPicksEffect.TvShowNavigation -> onNavigateTvShow(currentEffect.tvShowId)
            is TopTvShowsPicksEffect.BackNavigation -> onNavigateBack()
        }
    }
}