package com.london.presentation.feature.details.actordetails.topmoviespicks

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
fun TopMoviesPicksScreen(
    onNavigateMovie: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: TopMoviesPicksViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    HandleTopMoviesPicksEffects(
        effect = effect,
        onNavigateMovie = onNavigateMovie,
        onNavigateBack = onNavigateBack
    )
    BuildScreen {
        when {
            state.isSaved -> LoadingScreen()
            state.errorState != null -> NetworkErrorScreen()
            else -> TopMoviesPicksContent(
                state = state,
                contract = viewModel,
            )
        }
    }
}

@Composable
private fun TopMoviesPicksContent(
    state: TopMoviesPicksUiState,
    contract: TopMoviesPicksContract,
    modifier: Modifier = Modifier,
) {
    MediaLazyGrid(
        title = stringResource(R.string.top_movies_picks),
        items = state.movieDetails.cast,
        onBack = contract::onBack,
        getImageUrl = { it.posterUrl },
        onSaveClick = { contract.onSaveMovie(it.id) },
        modifier = modifier
    )
}

@Composable
private fun HandleTopMoviesPicksEffects(
    effect: TopMoviesPicksEffect?,
    onNavigateMovie: (Int) -> Unit,
    onNavigateBack: () -> Unit,
) {
    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TopMoviesPicksEffect.NavigateBack -> onNavigateBack()
            is TopMoviesPicksEffect.NavigationToMovieDetails -> onNavigateMovie(currentEffect.movieId)
        }
    }
}
