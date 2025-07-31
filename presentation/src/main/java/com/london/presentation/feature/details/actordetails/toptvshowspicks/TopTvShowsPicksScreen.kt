package com.london.presentation.feature.details.actordetails.toptvshowspicks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.presentation.R
import com.london.presentation.feature.buildscreen.BuildScreen
import com.london.presentation.feature.buildscreen.LoadingScreen
import com.london.presentation.feature.buildscreen.NetworkErrorScreen
import com.london.presentation.shared.MediaLazyGrid
import com.london.presentation.utils.Listen

@Composable
fun TopTvShowsPicksScreen(
    onNavigateTvShow: (tvShowId: Int) -> Unit,
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

    BuildScreen {
        when {
            state.isSaved -> LoadingScreen()
            state.errorState != null -> NetworkErrorScreen()
            else -> TopTvShowsPicksContent(
                state = state,
                contract = viewModel,
            )
        }
    }
}

@Composable
private fun TopTvShowsPicksContent(
    state: TopTvShowsPicksUiState,
    contract: TopTvShowsPicksContract,
    modifier: Modifier = Modifier,
) {
    MediaLazyGrid(
        title = stringResource(R.string.top_movies_picks),
        items = state.tvShowDetails.cast,
        onBack = contract::onBack,
        getImageUrl = { it.posterUrl },
        onSaveClick = { contract.onSaveMovie(it.id) },
        modifier = modifier
    )
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