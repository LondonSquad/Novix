package com.london.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.EmptyLayout
import com.london.designsystem.component.TopBar
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
fun <T> MediaLazyGrid(
    title: String,
    items: List<T>,
    onBack: () -> Unit,
    getImageUrl: (T) -> String,
    modifier: Modifier = Modifier,
    onItemClick: (T) -> Unit = {},
    onSavedClick: (T) -> Unit = {},
    isItemSaved: (T) -> Boolean = { false },
    isLoading: Boolean = false,
    emptyTitle: String = "",
    emptyImage: Int? = null,
    myRatingList: Boolean = false,
    rate: String = "5",
    onDeleteClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 12.dp
                ),
            title = title,
            onBackClick = onBack
        )

        when {
            isLoading -> CircularLoading(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )

            !isLoading && items.isEmpty() && emptyImage != null -> EmptyLayout(
                text = emptyTitle,
                image = emptyImage,
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
            )

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(gridColumns()),
                    contentPadding = PaddingValues(
                        top = 12.dp,
                        bottom = 16.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp)
                ) {
                    items(items) { item ->
                        HomeCard(
                            imageUrl = getImageUrl(item),
                            isSaved = isItemSaved(item),
                            onSaveClick = { onSavedClick(item) },
                            rate = rate,
                            onDeleteClick = onDeleteClick,
                            modifier = Modifier.clickable { onItemClick(item) }
                        )
                    }
                }
            }
        }
    }
}

