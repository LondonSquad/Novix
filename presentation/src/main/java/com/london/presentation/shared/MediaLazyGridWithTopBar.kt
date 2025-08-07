package com.london.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Movie

@Composable
fun <T : Any> MediaLazyGridWithTopBar(
    title: String,
    onBack: () -> Unit,
    getImageUrl: (T) -> String,
    modifier: Modifier = Modifier,
    items: List<T>? = null,
    pagingItems: LazyPagingItems<T>? = null,
    onSaveClick: (T) -> Unit = {},
    isItemSaved: (T) -> Boolean = { false },
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            title = title,
            onBackClick = onBack
        )

        MediaLazyVerticalGrid(
            items = items,
            pagingItems = pagingItems,
            getImageUrl = getImageUrl,
            getTitle = { it.toString() },
            onItemClick = { onSaveClick(it) },
            onSaveClick = onSaveClick,
            isItemSaved = isItemSaved,
            modifier = Modifier.fillMaxSize()
        )
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

    MediaLazyGridWithTopBar(
        title = "Sample Movies",
        items = sampleMovies,
        onBack = {},
        getImageUrl = { it.posterUrl },
        onSaveClick = {},
        isItemSaved = { false }
    )
}
