package com.london.presentation.shared.container

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.domain.entity.genre.MovieGenre
import com.london.presentation.shared.HomeCard
import com.london.presentation.utils.gridColumns

@Composable
fun <T : Any> MediaLazyVerticalGrid(
    items: List<T>,
    modifier: Modifier = Modifier,
    imageUrl: (T) -> String? = { it.getImageUrl() },
    name: (T) -> String = { it.getName() },
    hasSaveIcon: Boolean = true,
    onSaveClick: (T) -> Unit = {},
    isItemSaved: (T) -> Boolean = { false },
    onDeleteClick: (T) -> Unit = {},
    myRatingList: Boolean = false,
    rate: String = "3",
    onNavigateToMovie: (Int) -> Unit = {},
    onNavigateToTvShow: (Int) -> Unit = {},
    topBar: @Composable (() -> Unit)? = null
) {
    AnimatedGrid(
        modifier = modifier,
        topBar = topBar,
        targetKey = items.size
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
                HomeGridCard(
                    item = item,
                    imageUrl = url,
                    name = name,
                    isItemSaved = isItemSaved,
                    hasSaveIcon = hasSaveIcon,
                    onSaveClick = onSaveClick,
                    onDeleteClick = onDeleteClick,
                    myRatingList = myRatingList,
                    rate = rate,
                    onNavigateToMovie = onNavigateToMovie,
                    onNavigateToTvShow = onNavigateToTvShow
                )
            }
        }
    }
}

@Composable
fun <T : Any> MediaLazyVerticalGrid(
    pagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    imageUrl: (T) -> String? = { it.getImageUrl() },
    name: (T) -> String = { it.getName() },
    hasSaveIcon: Boolean = true,
    onSaveClick: (T) -> Unit = {},
    isItemSaved: (T) -> Boolean = { false },
    onDeleteClick: (T) -> Unit = {},
    myRatingList: Boolean = false,
    rate: String = "3",
    topBar: @Composable (() -> Unit)? = null,
    onNavigateToMovie: (Int) -> Unit = {},
    onNavigateToTvShow: (Int) -> Unit = {}
) {
    AnimatedGrid(
        modifier = modifier,
        topBar = topBar,
        targetKey = pagingItems.itemSnapshotList.items.size
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
            RenderPagingItem(
                index = index,
                pagingItems = pagingItems,
                imageUrl = imageUrl,
                name = name,
                isItemSaved = isItemSaved,
                hasSaveIcon = hasSaveIcon,
                onSaveClick = onSaveClick,
                onDeleteClick = onDeleteClick,
                myRatingList = myRatingList,
                rate = rate,
                onNavigateToMovie = onNavigateToMovie,
                onNavigateToTvShow = onNavigateToTvShow,
            )
        }
    }
}

@Composable
private fun <T : Any> RenderPagingItem(
    index: Int,
    pagingItems: LazyPagingItems<T>,
    imageUrl: (T) -> String?,
    name: (T) -> String,
    isItemSaved: (T) -> Boolean,
    hasSaveIcon: Boolean,
    onSaveClick: (T) -> Unit,
    onDeleteClick: (T) -> Unit,
    myRatingList: Boolean,
    rate: String,
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
                onSaveClick = { onSaveClick(item) },
                onDeleteClick = { onDeleteClick(item) },
                myRatingList = myRatingList,
                rate = rate
            )
        }
    }
}

@Composable
private fun AnimatedGrid(
    modifier: Modifier,
    topBar: @Composable (() -> Unit)?,
    targetKey: Int,
    content: LazyGridScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {
        topBar?.invoke()

        AnimatedContent(
            targetState = targetKey,
            transitionSpec = gridTransitionSpec()
        ) { keyValue ->
            key(keyValue) {
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
    }
}

@Composable
private fun <T : Any> HomeGridCard(
    item: T,
    imageUrl: String,
    name: (T) -> String,
    isItemSaved: (T) -> Boolean,
    hasSaveIcon: Boolean,
    onSaveClick: (T) -> Unit,
    onDeleteClick: (T) -> Unit,
    myRatingList: Boolean,
    rate: String,
    onNavigateToMovie: (Int) -> Unit,
    onNavigateToTvShow: (Int) -> Unit
) {
    HomeCard(
        imageUrl = imageUrl,
        imageDescription = name(item),
        isSaved = isItemSaved(item),
        hasSaveIcon = hasSaveIcon,
        onSaveClick = { onSaveClick(item) },
        onDeleteClick = { onDeleteClick(item) },
        myRatingList = myRatingList,
        rate = rate,
        modifier = Modifier.clickable {
            when (item) {
                is Movie -> onNavigateToMovie(item.id)
                is TvShow -> onNavigateToTvShow(item.id)
            }
        }
    )
}

private fun gridTransitionSpec(): AnimatedContentTransitionScope<Int>.() -> ContentTransform = {
    (slideInVertically(
        animationSpec = tween(1100),
        initialOffsetY = { it }
    ) + fadeIn(tween(1100))) togetherWith
            (slideOutVertically(
                animationSpec = tween(1000),
                targetOffsetY = { -it }
            ) + fadeOut(tween(1000)))
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
