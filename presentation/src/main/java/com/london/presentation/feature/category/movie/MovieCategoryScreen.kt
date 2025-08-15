package com.london.presentation.feature.category.movie

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R
import com.london.presentation.shared.MediaLazyPagingGrid
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.utils.Listen
import com.london.presentation.utils.isLoading

@Composable
fun MoviesByCategoryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMovieDetails: (Int) -> Unit,
    viewModel: MovieCategoryViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            MovieCategoryEffect.BackNavigation -> onNavigateBack()
            is MovieCategoryEffect.MovieDetailsNavigation -> onNavigateToMovieDetails(
                currentEffect.movieId
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
    state: MovieCategoryUiState,
    contract: MovieCategoryContract,
) {

    val moviesLazyList = state.moviesFlow.collectAsLazyPagingItems()
    BuildScreen(
        onBack = contract::onBack,
        isLoading = moviesLazyList.isLoading(),
        isError = moviesLazyList.loadState.refresh is LoadState.Error,
        onRetry = moviesLazyList::refresh,
        emptyLayoutMessage = R.string.there_is_no_items_for_this_genre,
        emptyLayoutImage = R.drawable.empty
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(top = 12.dp)
        ) {
            TopBar(
                title = stringResource(
                    state.genre.stringResId
                ),
                onBackClick = contract::onBack,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
            )

            MediaLazyPagingGrid(
                pagingFlow = moviesLazyList,
                onItemClick = { contract.onMovieClick(it.id) },
                getImageUrl = { it.posterUrl },
                getTitle = { "${it.name} movie img" },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onSaveClick = { /* TODO: Implement save functionality */ },
                isItemSaved = { false },
            )
        }
    }
}

@ThemePreviews
@Composable
private fun MoviesByCategoryContentPreview() {
    Content(
        state = MovieCategoryUiState(),
        contract = object : MovieCategoryContract {
            override fun onSavedClick(movieId: Int) {}
            override fun onMovieClick(movieId: Int) {}
            override fun onBack() {}
        },
    )
}
