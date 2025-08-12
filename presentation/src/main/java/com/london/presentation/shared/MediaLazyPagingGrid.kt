package com.london.presentation.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.utils.gridColumns

@Deprecated(
    message = "Use MediaLazyVerticalGrid or MediaLazyGridWithFilter instead. This composable will be removed in a future version. Note: Both have two overloads - one for List<T> and one for LazyPagingItems<T>.",
    replaceWith = ReplaceWith(
        expression = "MediaLazyGridWithFilter(" +
                "items = items, " +
                "modifier = modifier, " +
                "imageUrl = getImageUrl, " +
                "name = { it.getName() }, " +
                "onSaveClick = onSavedClick, " +
                "isItemSaved = isItemSaved, " +
                "onDeleteClick = onDeleteClick, " +
                "onMovieGenreClick = {}, " +
                "onTvShowGenreClick = {}, " +
                "config = MediaGridConfig(" +
                "    showSaveIcon = hasSaveIcon, " +
                "    isDarkMode = isDarkMode, " +
                "    myRatingList = myRatingList, " +
                "    rate = rate, " +
                "    isMovieSelected = true, " +
                "    isTvShowSelected = false, " +
                "    selectedMovieGenre = MovieGenre.All, " +
                "    selectedTvShowGenre = TvShowGenre.All, " +
                "    onNavigateToMovie = onItemClick, " +
                "), " +
                "topBar = {" +
                "    DefaultAppTopBar(" +
                "        title = title," +
                "        onBack = onBack" +
                "    )" +
                "}" +
                ")",
        imports = ["com.london.presentation.shared.container.MediaLazyGridWithFilter", "com.london.presentation.shared.container.MediaGridConfig", "com.london.presentation.utils.MovieGenre", "com.london.presentation.utils.TvShowGenre"]
    ),
    level = DeprecationLevel.WARNING
)

@Composable
fun <T : Any> MediaLazyPagingGrid(
    pagingFlow: LazyPagingItems<T>,
    onItemClick: (T) -> Unit,
    getImageUrl: (T) -> String,
    getTitle: (T) -> String,
    modifier: Modifier = Modifier,
    onSaveClick: (T) -> Unit = {},
    isItemSaved: (T) -> Boolean = { false },
) {
    LazyVerticalGrid(
        state = rememberLazyGridState(),
        columns = GridCells.Fixed(gridColumns()),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(pagingFlow.itemCount) { index ->
            val item = pagingFlow[index]
            if (item != null) {
                HomeCard(
                    imageUrl = getImageUrl(item),
                    onSaveClick = { onSaveClick(item) },
                    isSaved = isItemSaved(item),
                    imageDescription = getTitle(item),
                    modifier = Modifier.clickable { onItemClick(item) },
                    isDarkMode = NovixTheme.isThemeDark
                )
            }
        }
    }
}