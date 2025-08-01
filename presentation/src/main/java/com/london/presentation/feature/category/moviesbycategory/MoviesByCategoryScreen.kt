package com.london.presentation.feature.category.moviesbycategory

import androidx.compose.foundation.layout.Column
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
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Movie
import com.london.presentation.feature.buildscreen.BuildScreen
import com.london.presentation.feature.search.SearchCategory
import com.london.presentation.shared.MediaLazyPagingGrid
import com.london.presentation.utils.Listen
import com.london.presentation.utils.convertGenreCodeToString
import kotlinx.coroutines.flow.flow

@Composable
fun MoviesByCategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: MoviesByCategoryViewModel = hiltViewModel(),
    onNavigateToMovieDetails: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            MoviesByCategoryEffect.NavigateBack -> onNavigateBack()
            is MoviesByCategoryEffect.NavigateToMovieDetails -> onNavigateToMovieDetails(
                currentEffect.movieId
            )
        }
    }

    BuildScreen(
        onBack = viewModel::onBack,
        isLoading = state.isLoading,
        isError = state.error != null
    ) {
        MoviesByCategoryContent(
                state = state,
                contract = viewModel,
                modifier = modifier,
            )
    }
}

@Composable
private fun MoviesByCategoryContent(
    state: MoviesByCategoryUiState,
    contract: MoviesByCategoryContract,
    modifier: Modifier = Modifier
) {

    val moviesLazyList = state.movies.collectAsLazyPagingItems()
    Column {
        TopBar(
            title = stringResource(
                convertGenreCodeToString(
                    genreId = state.categoryId, searchCategory = SearchCategory.Movies
                )
            ), onBackClick = contract::onBack,
            modifier = modifier
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)

        )
        MediaLazyPagingGrid<Movie>(
            pagingFlow = moviesLazyList,
            onItemClick = { contract.onMovieClick(it.id) },
            getImageUrl = { it.posterUrl },
            getTitle = { "${it.name} movie img" },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onSaveClick = { /* TODO: Implement save functionality */ },
            isItemSaved = { false }
        )
    }
}

@ThemePreviews
@Composable
private fun MoviesByCategoryContentPreview() {
    MoviesByCategoryContent(
        state = MoviesByCategoryUiState(
            movies = flow<PagingData<Movie>> {
                Movie(
                    id = 1,
                    name = "",
                    posterUrl = "",
                    releaseYear = 1,
                    rating = 3,
                    genreIds = listOf()
                )
                Movie(
                    id = 1,
                    name = "",
                    posterUrl = "",
                    releaseYear = 1,
                    rating = 3,
                    genreIds = listOf()
                )
                Movie(
                    id = 1,
                    name = "",
                    posterUrl = "",
                    releaseYear = 1,
                    rating = 3,
                    genreIds = listOf()
                )
                Movie(
                    id = 1,
                    name = "",
                    posterUrl = "",
                    releaseYear = 1,
                    rating = 3,
                    genreIds = listOf()
                )
                Movie(
                    id = 1,
                    name = "",
                    posterUrl = "",
                    releaseYear = 1,
                    rating = 3,
                    genreIds = listOf()
                )
                Movie(
                    id = 1,
                    name = "",
                    posterUrl = "",
                    releaseYear = 1,
                    rating = 3,
                    genreIds = listOf()
                )
                Movie(
                    id = 1,
                    name = "",
                    posterUrl = "",
                    releaseYear = 1,
                    rating = 3,
                    genreIds = listOf()
                )
                Movie(
                    id = 1,
                    name = "",
                    posterUrl = "",
                    releaseYear = 1,
                    rating = 3,
                    genreIds = listOf()
                )
            }),
        contract = object : MoviesByCategoryContract {
            override fun onSavedClick(movieId: Int) {}
            override fun onMovieClick(movieId: Int) {}
            override fun onBack() {}
        },
    )
}