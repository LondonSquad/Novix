package com.london.presentation.screen.category.moviesbycategory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Movie
import com.london.presentation.screen.search.SearchCategory
import com.london.presentation.utils.convertGenreCodeToString
import kotlinx.coroutines.flow.flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesByCategoryScreen(
    modifier: Modifier = Modifier, viewModel: MoviesByCategoryViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    MoviesByCategoryContent(
        state = state, interactions = viewModel, modifier = modifier
    )
}

@Composable
private fun MoviesByCategoryContent(
    state: MoviesByCategoryUiState,
    interactions: MoviesByCategoryInteractions,
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
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) }) {
            TopBar(
                title = stringResource(
                    convertGenreCodeToString(
                        genreId = state.categoryId, searchCategory = SearchCategory.Movies
                    )
                ), onBackClick = interactions::onBackClick
            )
        }
        items(moviesLazyList.itemCount) { index ->
            val movie = moviesLazyList[index]
            if (movie != null) HomeCard(
                imageUrl = movie.posterPicture,
                isSaved = false,
                onSaveClick = { interactions.onSavedClick(movie.id) })
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
                posterPicture = "",
                releaseYear = 1,
                rating = 3,
                genreIds = listOf()
            )
            Movie(
                id = 1,
                name = "",
                posterPicture = "",
                releaseYear = 1,
                rating = 3,
                genreIds = listOf()
            )
            Movie(
                id = 1,
                name = "",
                posterPicture = "",
                releaseYear = 1,
                rating = 3,
                genreIds = listOf()
            )
            Movie(
                id = 1,
                name = "",
                posterPicture = "",
                releaseYear = 1,
                rating = 3,
                genreIds = listOf()
            )
            Movie(
                id = 1,
                name = "",
                posterPicture = "",
                releaseYear = 1,
                rating = 3,
                genreIds = listOf()
            )
            Movie(
                id = 1,
                name = "",
                posterPicture = "",
                releaseYear = 1,
                rating = 3,
                genreIds = listOf()
            )
            Movie(
                id = 1,
                name = "",
                posterPicture = "",
                releaseYear = 1,
                rating = 3,
                genreIds = listOf()
            )
            Movie(
                id = 1,
                name = "",
                posterPicture = "",
                releaseYear = 1,
                rating = 3,
                genreIds = listOf()
            )
        }), interactions = object : MoviesByCategoryInteractions {
        override fun onMovieClick(movieId: Int) {}
        override fun onBackClick() {}
        override fun onSavedClick(movieId: Int) {}
    })
}