package com.london.presentation.shared.container

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Movie
import com.london.presentation.shared.HomeCard

@Composable
fun <T : Any> MediaLazyVerticalGrid(
    items: List<T>,
    imageUrl: (T) -> String,
    name: (T) -> String,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    hasSaveIcon: Boolean = true,
    onSaveClick: (T) -> Unit = {},
    isItemSaved: (T) -> Boolean = { false },
    onDeleteClick: (T) -> Unit = {},
    isDarkMode: Boolean = true,
    myRatingList: Boolean = false,
    rate: String = "3",
    topBar: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {
        topBar?.invoke()

        LazyVerticalGrid(
            state = rememberLazyGridState(),
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(items) { item ->
                HomeCard(
                    imageUrl = imageUrl(item),
                    modifier = Modifier.clickable { onItemClick(item) },
                    imageDescription = name(item),
                    isSaved = isItemSaved(item),
                    hasSaveIcon = hasSaveIcon,
                    onSaveClick = { onSaveClick(item) },
                    onDeleteClick = { onDeleteClick(item) },
                    isDarkMode = isDarkMode,
                    myRatingList = myRatingList,
                    rate = rate
                )
            }
        }
    }
}

@Composable
fun <T : Any> MediaLazyVerticalGrid(
    pagingItems: LazyPagingItems<T>,
    imageUrl: (T) -> String,
    name: (T) -> String,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    hasSaveIcon: Boolean = true,
    onSaveClick: (T) -> Unit = {},
    isItemSaved: (T) -> Boolean = { false },
    onDeleteClick: (T) -> Unit = {},
    isDarkMode: Boolean = true,
    myRatingList: Boolean = false,
    rate: String = "3",
    topBar: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {
        topBar?.invoke()
        LazyVerticalGrid(
            state = rememberLazyGridState(),
            columns = GridCells.Fixed(2),
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(pagingItems.itemCount) { index ->
                pagingItems[index]?.let { item ->
                    HomeCard(
                        imageUrl = imageUrl(item),
                        modifier = Modifier.clickable { onItemClick(item) },
                        imageDescription = name(item),
                        isSaved = isItemSaved(item),
                        hasSaveIcon = hasSaveIcon,
                        onSaveClick = { onSaveClick(item) },
                        onDeleteClick = { onDeleteClick(item) },
                        isDarkMode = isDarkMode,
                        myRatingList = myRatingList,
                        rate = rate
                    )
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun Preview() {

    val sampleMovies = listOf(
        Movie(
            id = 1,
            name = "Movie One",
            posterUrl = "https://example.com/movie1.jpg",
            releaseYear = 2023,
            rating = 8,
            genreIds = listOf(28, 12),
        ),
        Movie(
            id = 2,
            name = "Movie Two",
            posterUrl = "https://example.com/movie2.jpg",
            releaseYear = 2024,
            rating = 7,
            genreIds = listOf(18, 35)
        )
    )

    MediaLazyVerticalGrid(
        items = sampleMovies,
        imageUrl = { it.posterUrl },
        name = { it.name },
        onItemClick = {},
        onSaveClick = {},
        isItemSaved = { false }
    )
}
