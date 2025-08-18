package com.london.presentation.feature.home.trending.movie

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.shared.DefaultAppTopBar
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.shared.container.MediaLazyGridWithFilter
import com.london.presentation.shared.genre.MovieGenreUi
import com.london.presentation.utils.Listen
import com.london.presentation.utils.isLoading

@Composable
fun TrendingMoviesScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMovieDetails: (Int) -> Unit,
    viewModel: TrendingMoviesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TrendingMoviesEffect.MovieDetailsNavigation -> onNavigateToMovieDetails(
                currentEffect.movieId
            )

            is TrendingMoviesEffect.BackNavigation -> onNavigateBack()
        }
    }

    Content(
        state = state,
        contract = viewModel,
    )
}

@Composable
private fun Content(
    state: TrendingMoviesUiState = TrendingMoviesUiState(),
    contract: TrendingMoviesContract,
) {
    val moviesLazyItems = state.moviesFlow.collectAsLazyPagingItems()

    BuildScreen(
        isLoading = moviesLazyItems.isLoading(),
        isError = moviesLazyItems.loadState.refresh is LoadState.Error,
        onBack = contract::onBackClick,
        onRetry = contract::onRetryClick,
        emptyLayoutMessage = R.string.no_trending_movies_in_genre,
        emptyLayoutImage = R.drawable.img_no_result,
        pagingFlow = moviesLazyItems
    ) {
        MediaLazyGridWithFilter(
            pagingItems = moviesLazyItems,
            imageUrl = { it.posterPath },
            name = { it.title },
            isLoading = moviesLazyItems.isLoading(),
            topBar = {
                DefaultAppTopBar(
                    title = stringResource(R.string.trending_movies),
                    onBackClick = contract::onBackClick
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun Preview() = NovixTheme {
    Content(
        state = TrendingMoviesUiState(),
        contract = object : TrendingMoviesContract {
            override fun onBackClick() {}
            override fun onRetryClick() {}
            override fun onMovieClick(id: Int) {}
            override fun onGenreClick(genre: MovieGenreUi) {}
        }
    )
}
