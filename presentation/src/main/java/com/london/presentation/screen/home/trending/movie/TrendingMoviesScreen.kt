package com.london.presentation.screen.home.trending.movie

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.EmptyLayout
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.screen.home.trending.GenresSection
import com.london.presentation.utils.Listen
import com.london.presentation.utils.MovieGenre
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrendingMoviesScreen(
    onMovieClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: TrendingMoviesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsStateWithLifecycle(null)
    val screenWidth = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp }

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TrendingMoviesEffect.NavigateToMovie -> {
                onMovieClick(currentEffect.movieId)
            }

            is TrendingMoviesEffect.NavigateBack -> {
                onBackClick()
            }
        }
    }

    TrendingMoviesContent(
        state = state,
        screenWidth = screenWidth,
        contract = viewModel
    )
}

@Composable
private fun TrendingMoviesContent(
    screenWidth: Dp,
    state: TrendingMoviesUiState = TrendingMoviesUiState(),
    contract: TrendingMoviesContract = defaultTrendingMoviesContract()
) {
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
            onBackClick = contract::onBackClick
        )
        GenresSection(
            genres = MovieGenre.entries.toList(),
            selectedGenreId = state.selectedGenreId,
            screenWidth = screenWidth,
            onGenreClick = contract::onGenreSelected,
            modifier = Modifier.padding(bottom = 12.dp),
            getGenreId = { it.id },
            getGenreName = { stringResource(it.stringResId) }
        )

        val moviesLazyItems = state.trendingMovies.collectAsLazyPagingItems()
        val isLoading = moviesLazyItems.loadState.refresh is androidx.paging.LoadState.Loading
        val filteredMovies = List(moviesLazyItems.itemCount) { moviesLazyItems[it] }
            .filterNotNull()
            .filter { movie ->
                state.selectedGenreId == null ||
                        state.selectedGenreId == MovieGenre.All.id ||
                        movie.genreIds.contains(state.selectedGenreId)
            }
        val gridState = rememberLazyGridState()

        LaunchedEffect(state.selectedGenreId) {
            gridState.scrollToItem(0)
        }

        isLoading.takeIf { it }?.let {
            CircularLoading(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }

        (!isLoading).takeIf { it && filteredMovies.isNotEmpty() }?.let {
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(filteredMovies.size) { index ->
                    val movie = filteredMovies[index]
                    HomeCard(
                        imageUrl = movie.posterPath,
                        isSaved = false,
                        onSaveClick = {},
                        modifier = Modifier.clickable { contract.onMovieClick(movie.id) }
                    )
                }
            }
        }

        (!isLoading && filteredMovies.isEmpty()).takeIf { it }?.let {
            EmptyLayout(
                text = stringResource(R.string.no_trending_movies_in_genre),
                image = R.drawable.img_no_result,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}
