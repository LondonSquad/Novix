package com.london.presentation.feature.home.trending.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.shared.GenresSection
import com.london.presentation.shared.MediaLazyPagingGrid
import com.london.presentation.utils.Listen
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrendingMoviesScreen(
    onNavigateMovie: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: TrendingMoviesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TrendingMoviesEffect.NavigateToMovie -> onNavigateMovie(currentEffect.movieId)
            is TrendingMoviesEffect.NavigateBack -> onNavigateBack()
        }
    }

    TrendingMoviesContent(
        state = state,
        contract = viewModel
    )
}

@Composable
private fun TrendingMoviesContent(
    state: TrendingMoviesUiState = TrendingMoviesUiState(),
    contract: TrendingMoviesContract = defaultTrendingMoviesContract()
) {
    val screenWidth = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp }
    val moviesLazyItems = state.moviesFlow.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
    ) {
        TopBar(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            title = stringResource(R.string.trending_movies),
            onBackClick = contract::onBack
        )
        GenresSection(
            genres = state.movieGenres,
            selectedGenreId = state.selectedGenreId,
            screenWidth = screenWidth,
            onGenreClick = contract::onGenreSelected,
            modifier = Modifier.padding(bottom = 12.dp),
            getGenreId = { it.id },
            getGenreName = { stringResource(it.stringResId) }
        )
        MediaLazyPagingGrid(
            pagingFlow = moviesLazyItems,
            onItemClick = { contract.onMovieClick(it.id) },
            getImageUrl = { it.posterPath },
            getTitle = { it.title },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onSaveClick = { /* TODO: Implement save functionality */ },
            isItemSaved = { false }
        )
    }
}

@Preview
@Composable
private fun Preview() = NovixTheme {
    TrendingMoviesContent()
}
