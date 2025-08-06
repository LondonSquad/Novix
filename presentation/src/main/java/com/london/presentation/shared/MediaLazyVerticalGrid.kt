package com.london.presentation.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.london.designsystem.component.HomeCard
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Movie
import com.london.presentation.utils.gridColmuns

@Composable
fun <T : Any> MediaLazyVerticalGrid(
    getImageUrl: (T) -> String,
    onItemClick: (T) -> Unit,
    modifier: Modifier = Modifier,
    items: List<T>? = null,
    pagingItems: LazyPagingItems<T>? = null,
    getTitle: (T) -> String = { it.toString() },
    onSaveClick: (T) -> Unit = {},
    isItemSaved: (T) -> Boolean = { false },
) {
    LazyVerticalGrid(
        state = rememberLazyGridState(),
        columns = GridCells.Fixed(gridColmuns()),
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        if (pagingItems != null) {
            items(pagingItems.itemCount) { index ->
                pagingItems[index]?.let { item ->
                    HomeCard(
                        imageUrl = getImageUrl(item),
                        imageDescription = getTitle(item),
                        onSaveClick = { onSaveClick(item) },
                        isSaved = isItemSaved(item),
                        modifier = Modifier.clickable { onItemClick(item) }
                    )
                }
            }
        } else if (items != null) {
            items(items) { item ->
                HomeCard(
                    imageUrl = getImageUrl(item),
                    imageDescription = getTitle(item),
                    onSaveClick = { onSaveClick(item) },
                    isSaved = isItemSaved(item),
                    modifier = Modifier.clickable { onItemClick(item) }
                )
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
            name = "Movie 1",
            posterUrl = "https://example.com/movie1.jpg",
            releaseYear = 2023,
            rating = 8,
            genreIds = listOf(28, 12)
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
        getImageUrl = { it.posterUrl },
        getTitle = { it.name },
        onItemClick = {},
        onSaveClick = {},
        isItemSaved = { false }
    )
}
