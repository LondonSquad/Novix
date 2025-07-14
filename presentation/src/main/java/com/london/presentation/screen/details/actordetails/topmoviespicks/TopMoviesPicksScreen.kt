package com.london.presentation.screen.details.actordetails.topmoviespicks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.london.presentation.R
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Movie
import org.koin.androidx.compose.koinViewModel

@Composable
fun TopMoviesPicksScreen(
    modifier: Modifier = Modifier,
    viewModel: TopMoviesPicksViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    TopMoviesPicksContent(
        state = state,
        interactions = viewModel,
        modifier = modifier
    )
}

@Composable
private fun TopMoviesPicksContent(
    state: TopMoviesPicksUiState,
    interactions: TopMoviesPicksInteractions,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
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
                title = stringResource(R.string.top_movies_picks),
                onBackClick = interactions::onBackClick
            )
        }
        items(state.movies) { movie ->
            HomeCard(
                imageUrl = movie.posterPicture,
                isSaved = false,
                onSaveClick = { interactions.onSavedClick(movie.id) })
        }
    }
}

@Preview
@Composable
fun TopMoviesPicksPreview() {
    TopMoviesPicksContent(
        state = TopMoviesPicksUiState(
            movies = listOf(
                Movie(
                    id = 1,
                    name = "",
                    posterPicture = "https://image.tmdb.org/t/p/w500//8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
                    releaseYear = 1,
                    rating = 3,
                    genreIds = listOf()
                ),
                Movie(
                    id = 1,
                    name = "",
                    posterPicture = "https://image.tmdb.org/t/p/w500//8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
                    releaseYear = 1,
                    rating = 3,
                    genreIds = listOf()
                ),
                Movie(
                    id = 1,
                    name = "",
                    posterPicture = "https://image.tmdb.org/t/p/w500//8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
                    releaseYear = 1,
                    rating = 3,
                    genreIds = listOf()
                ),
                Movie(
                    id = 1,
                    name = "",
                    posterPicture = "https://image.tmdb.org/t/p/w500//8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
                    releaseYear = 1,
                    rating = 3,
                    genreIds = listOf()
                ),
                Movie(
                    id = 1,
                    name = "",
                    posterPicture = "https://image.tmdb.org/t/p/w500//8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
                    releaseYear = 1,
                    rating = 3,
                    genreIds = listOf()
                ),
                Movie(
                    id = 1,
                    name = "",
                    posterPicture = "https://image.tmdb.org/t/p/w500//8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
                    releaseYear = 1,
                    rating = 3,
                    genreIds = listOf()
                ),
                Movie(
                    id = 1,
                    name = "",
                    posterPicture = "https://image.tmdb.org/t/p/w500//8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
                    releaseYear = 1,
                    rating = 3,
                    genreIds = listOf()
                ),
                Movie(
                    id = 1,
                    name = "",
                    posterPicture = "https://image.tmdb.org/t/p/w500//8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
                    releaseYear = 1,
                    rating = 3,
                    genreIds = listOf()
                ),

            )
        ),
        interactions = object : TopMoviesPicksInteractions {
            override fun onMovieClick(movieId: Int) {}
            override fun onBackClick() {}
            override fun onSavedClick(movieId: Int) {}
        }
    )
}