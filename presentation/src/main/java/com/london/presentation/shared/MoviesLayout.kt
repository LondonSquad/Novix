package com.london.presentation.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.london.domain.entity.Movie
import com.london.presentation.utils.gridColmuns

@Composable
fun MoviesLayOut(
    movieUis: LazyPagingItems<Movie>,
    onSaveClick: (Movie) -> Unit,
    isMovieSaved: (Movie) -> Boolean,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridColmuns()),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(movieUis.itemCount) { index ->
            val movie = movieUis[index]
            if (movie != null) {
                HomeCard(
                    imageUrl = movie.posterUrl,
                    onSaveClick = { onSaveClick(movie) },
                    isSaved = isMovieSaved(movie),
                    imageDescription = movie.name,
                    modifier = Modifier.clickable { onMovieClick(movie) }
                )
            }
        }
    }
}