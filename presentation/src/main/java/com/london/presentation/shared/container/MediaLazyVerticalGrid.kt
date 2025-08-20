package com.london.presentation.shared.container

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.entity.movie.Movie
import com.london.domain.entity.tvshow.TvShow
import com.london.presentation.shared.HomeCard
import com.london.presentation.utils.gridColumns

@Composable
fun <T : Any> MediaLazyVerticalGrid(
    items: List<T>,
    modifier: Modifier = Modifier,
    hasSaveIcon: Boolean = true,
    onSaveClick: (T) -> Unit = {},
    onDeleteClick: (T) -> Unit = {},
    rate: (T) -> String? = { null },
    onItemClick: ((T) -> Unit)? = null,
    onNavigateToMovie: (Int) -> Unit = {},
    onNavigateToTvShow: (Int) -> Unit = {},
    name: (T) -> String = { it.getName() },
    isItemSaved: (T) -> Boolean = { false },
    topBar: @Composable (() -> Unit)? = null,
    imageUrl: (T) -> String? = { it.getImageUrl() },
) {
    MediaGridContainer(
        modifier = modifier,
        topBar = topBar,
    ) {
        items(
            items = items,
            key = { item ->
                when (item) {
                    is Movie -> item.id
                    is TvShow -> item.id
                    else -> item.hashCode()
                }
            }
        ) { item ->
            imageUrl(item)?.let { url ->
                HomeCard(
                    imageUrl = url,
                    imageDescription = name(item),
                    isSaved = isItemSaved(item),
                    hasSaveIcon = hasSaveIcon,
                    onSaveClick = { onSaveClick(item) },
                    onDeleteClick = { onDeleteClick(item) },
                    rate = rate(item),
                    modifier = Modifier.navigationClickable(
                        item = item,
                        onItemClick = onItemClick,
                        onNavigateToMovie = onNavigateToMovie,
                        onNavigateToTvShow = onNavigateToTvShow
                    )
                )
            }
        }
    }
}

@Composable
fun <T : Any> MediaLazyVerticalGrid(
    pagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    rate: (T) -> String? = { null },
    name: (T) -> String = { it.getName() },
    imageUrl: (T) -> String? = { it.getImageUrl() },
    hasSaveIcon: Boolean = true,
    onSaveClick: (Int) -> Unit = {},
    isItemSaved: (T) -> Boolean = { false },
    onDeleteClick: (T) -> Unit = {},
    topBar: @Composable (() -> Unit)? = null,
    onNavigateToMovie: (Int) -> Unit = {},
    onNavigateToTvShow: (Int) -> Unit = {}
) {
    MediaGridContainer(
        modifier = modifier,
        topBar = topBar,
    ) {
        items(
            count = pagingItems.itemCount,
            key = { index ->
                when (val item = pagingItems[index]) {
                    is Movie -> item.id
                    is TvShow -> item.id
                    else -> index
                }
            }
        ) { index ->
            RenderPagingGridItem(
                index = index,
                pagingItems = pagingItems,
                imageUrl = imageUrl,
                name = name,
                isItemSaved = isItemSaved,
                hasSaveIcon = hasSaveIcon,
                onSaveClick = onSaveClick,
                onDeleteClick = onDeleteClick,
                rate = rate,
                onNavigateToMovie = onNavigateToMovie,
                onNavigateToTvShow = onNavigateToTvShow,
            )
        }
    }
}

@Composable
private fun MediaGridContainer(
    modifier: Modifier,
    topBar: @Composable (() -> Unit)?,
    content: LazyGridScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {
        topBar?.invoke()

        LazyVerticalGrid(
            state = rememberLazyGridState(),
            columns = GridCells.Fixed(gridColumns()),
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            content = content
        )
    }
}

@Composable
private fun <T : Any> RenderPagingGridItem(
    index: Int,
    pagingItems: LazyPagingItems<T>,
    imageUrl: (T) -> String?,
    name: (T) -> String,
    isItemSaved: (T) -> Boolean,
    hasSaveIcon: Boolean,
    onSaveClick: (Int) -> Unit,
    onDeleteClick: (T) -> Unit,
    rate: (T) -> String?,
    onNavigateToMovie: (Int) -> Unit,
    onNavigateToTvShow: (Int) -> Unit
) {
    pagingItems[index]?.let { item ->
        imageUrl(item)?.let {
            HomeCard(
                imageUrl = it,
                modifier = Modifier.clickable {
                    when (item) {
                        is Movie -> onNavigateToMovie(item.id)
                        is TvShow -> onNavigateToTvShow(item.id)
                    }
                },
                imageDescription = name(item),
                isSaved = isItemSaved(item),
                hasSaveIcon = hasSaveIcon,
                onSaveClick = { if (item is Movie) onSaveClick(item.id) },
                onDeleteClick = { onDeleteClick(item) },
                rate = rate(item)
            )
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
            genres = listOf(MovieGenre.TV_MOVIE),
        ),
        Movie(
            id = 2,
            name = "Movie Two",
            posterUrl = "https://example.com/movie2.jpg",
            releaseYear = 2024,
            rating = 7,
            genres = listOf(MovieGenre.TV_MOVIE)
        )
    )

    MediaLazyVerticalGrid(
        items = sampleMovies,
        imageUrl = { it.posterUrl },
        name = { it.name },
        onSaveClick = {},
        isItemSaved = { false },
        onNavigateToMovie = {},
        onNavigateToTvShow = {}
    )
}

@ThemePreviews
@Composable
private fun EmptyPreview() {
    MediaLazyVerticalGrid(
        items = emptyList<Movie>(),
        imageUrl = { it.posterUrl },
        name = { it.name },
        onSaveClick = {},
        isItemSaved = { false },
        onNavigateToMovie = {},
        onNavigateToTvShow = {}
    )
}
