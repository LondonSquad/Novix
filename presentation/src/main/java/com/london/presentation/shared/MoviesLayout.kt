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
import com.london.designsystem.theme.NovixTheme
import com.london.domain.entity.Movie
import com.london.presentation.utils.gridColumns

@Deprecated(
    message = "Use MediaLazyVerticalGrid instead. This composable will be removed in a future version. Note: MediaLazyVerticalGrid has two overloads - one for List<T> and one for LazyPagingItems<T>.",
    replaceWith = ReplaceWith(
        expression = "MediaLazyVerticalGrid(" +
                "items = items, " +
                "modifier = modifier, " +
                "imageUrl = getImageUrl, " +
                "name = { it.getName() }, " +
                "hasSaveIcon = hasSaveIcon, " +
                "onSaveClick = onSaveClick, " +
                "isItemSaved = isItemSaved, " +
                "onDeleteClick = onDeleteClick, " +
                "isDarkMode = isDarkMode, " +
                "myRatingList = myRatingList, " +
                "rate = rate, " +
                "onNavigateToMovie = onItemClick, " +
                "topBar = {" +
                "    DefaultAppTopBar(" +
                "        title = title," +
                "        onBack = onBack" +
                "    )" +
                "}" +
                ")",
        imports = ["com.london.presentation.shared.container.MediaLazyVerticalGrid"]
    ),
    level = DeprecationLevel.WARNING
)

@Composable
fun MoviesLayOut(
    movieUis: LazyPagingItems<Movie>,
    onSaveClick: (Movie) -> Unit,
    isMovieSaved: (Movie) -> Boolean,
    onMovieClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridColumns()),
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
                    modifier = Modifier.clickable { onMovieClick(movie) },
                    isDarkMode = NovixTheme.isThemeDark
                )
            }
        }
    }
}