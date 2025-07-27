package com.london.presentation.feature.details.actordetails.topmoviespicks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.utils.Listen
import org.koin.androidx.compose.koinViewModel

@Composable
fun TopMoviesPicksScreen(
    modifier: Modifier = Modifier,
    viewModel: TopMoviesPicksViewModel = koinViewModel(),
    onMovieClick: (Int) -> Unit,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effects by viewModel.effect.collectAsState(null)

    effects?.Listen { currentEffect ->
        when (currentEffect) {
            TopMoviesPicksEffectUiState.NavigateBack -> onBackClick()
            is TopMoviesPicksEffectUiState.NavigationToMovieDetails ->
                onMovieClick(currentEffect.movieId)
        }
    }

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
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 158.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
            .padding(horizontal = 16.dp)
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) }) {
            TopBar(
                modifier = Modifier.statusBarsPadding(),
                title = stringResource(R.string.top_movies_picks),
                onBackClick = topMoviesPicksContract::onClickBack
            )
        }
        items(state.movieDetails.cast) { item ->
            HomeCard(
                imageUrl = item.posterUrl,
                isSaved = false,
                onSaveClick = { topMoviesPicksContract.onSaveMovie(item.id) },
                modifier = Modifier.clickable { topMoviesPicksContract.onSaveMovie(item.id) }
            )
        }
    }
}