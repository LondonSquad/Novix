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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.screen.home.GenresSection
import com.london.presentation.utils.Listen
import org.koin.androidx.compose.koinViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.presentation.screen.home.trending.movie.TrendingMoviesContract
import com.london.designsystem.component.EmptySearchLayout

@Composable
fun TrendingMoviesScreen(
    modifier: Modifier = Modifier,
    viewModel: TrendingMoviesViewModel = koinViewModel(),
    contract: TrendingMoviesContract
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsState()
    val density = LocalDensity.current
    val screenWidth = with(density) { LocalConfiguration.current.screenWidthDp.dp }

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TrendingMoviesEffect.NavigateToMovie -> {
                contract.onMovieClick(currentEffect.movieId)
                viewModel.resetEffect()
            }
            TrendingMoviesEffect.NavigateBack -> {
                contract.onBackClick()
                viewModel.resetEffect()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
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
            genres = state.genres,
            selectedGenreId = state.selectedGenreId,
            screenWidth = screenWidth,
            onGenreClick = { viewModel.onGenreSelected(it) },
            modifier = Modifier.padding(bottom = 12.dp)
        )

        val moviesLazyItems = state.trendingMovies.collectAsLazyPagingItems()
        val filteredMovies = if (state.selectedGenreId == null || state.selectedGenreId == com.london.presentation.utils.Genre.All.id) {
            (0 until moviesLazyItems.itemCount).map { moviesLazyItems[it] }.filterNotNull()
        } else {
            (0 until moviesLazyItems.itemCount).map { moviesLazyItems[it] }.filterNotNull().filter { it.genreIds.contains(state.selectedGenreId) }
        }
        if (filteredMovies.isEmpty()) {
            EmptySearchLayout(
                text = stringResource(R.string.no_trending_movies_in_genre),
                image = R.drawable.img_no_result,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } else {
            LazyVerticalGrid(
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
    }
}
