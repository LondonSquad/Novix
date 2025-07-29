package com.london.presentation.feature.category.moviesbycategory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Movie
import com.london.presentation.feature.buildscreen.BuildScreen
import com.london.presentation.feature.buildscreen.LoadingScreen
import com.london.presentation.feature.buildscreen.NetworkErrorScreen
import com.london.presentation.feature.search.SearchCategory
import com.london.presentation.utils.Listen
import com.london.presentation.utils.convertGenreCodeToString
import kotlinx.coroutines.flow.flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesByCategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: MoviesByCategoryViewModel = koinViewModel(),
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

    BuildScreen {
        when {
            state.isLoading -> LoadingScreen()
            state.error != null -> NetworkErrorScreen()
            else -> MoviesByCategoryContent(
                state = state,
                moviesByCategoryContract = viewModel,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun MoviesByCategoryContent(
    state: MoviesByCategoryUiState,
    moviesByCategoryContract: MoviesByCategoryContract,
    modifier: Modifier = Modifier
) {

    val moviesLazyList = state.movies.collectAsLazyPagingItems()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(top = 12.dp, bottom = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
            .padding(horizontal = 16.dp)
            .padding(paddingValues = WindowInsets.statusBars.asPaddingValues())
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) }) {
            TopBar(
                title = stringResource(
                    convertGenreCodeToString(
                        genreId = state.categoryId, searchCategory = SearchCategory.Movies
                    )
                ), onBackClick = moviesByCategoryContract::onBackClick
            )
        }
        items(moviesLazyList.itemCount) { index ->
            val movie = moviesLazyList[index]
            if (movie != null) HomeCard(
                imageUrl = movie.posterUrl,
                isSaved = false,
                onSaveClick = { moviesByCategoryContract.onSavedClick(movie.id) },
                modifier = Modifier.clickable {
                    moviesByCategoryContract.onMovieClick(movieId = movie.id)
                }
            )
        }
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
            }), moviesByCategoryContract = object : MoviesByCategoryContract {
            override fun onSavedClick(movieId: Int) {}
            override fun onMovieClick(movieId: Int) {}
            override fun onBackClick() {}
        },
    )
}