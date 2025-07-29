package com.london.presentation.feature.details.actordetails.topmoviespicks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.presentation.R
import com.london.presentation.shared.MediaLazyGrid
import com.london.presentation.utils.Listen
import org.koin.androidx.compose.koinViewModel

@Composable
fun TopMoviesPicksScreen(
    onNavigateMovie: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TopMoviesPicksViewModel = koinViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effects by viewModel.effect.collectAsState(null)

    HandleTopMoviesPicksEffects(
        effect = effects,
        onNavigateMovie = onNavigateMovie,
        onNavigateBack = onNavigateBack
    )

    TopMoviesPicksContent(
        state = uiState,
        topMoviesPicksContract = viewModel,
        modifier = modifier,
    )
}

@Composable
private fun TopMoviesPicksContent(
    state: TopMoviesPicksUiState,
    topMoviesPicksContract: TopMoviesPicksContract,
    modifier: Modifier = Modifier,
) {
    MediaLazyGrid(
        title = stringResource(R.string.top_movies_picks),
        movies = state.movieDetails.cast,
        onBackClick = topMoviesPicksContract::onClickBack,
        onMovieClick = topMoviesPicksContract::onSaveMovie,
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
