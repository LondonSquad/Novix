package com.london.presentation.shared.container

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.Movie
import com.london.domain.entity.TvShow
import com.london.presentation.shared.HomeCard

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
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            items(
                items = items,
                key = { item ->
                    when (item) {
                        is Movie -> "movie_${item.id}"
                        is TvShow -> "tv_show_${item.id}"
                        else -> item.hashCode().toString()
                    }
                }
            ) { item ->
                imageUrl(item)?.let { url ->
                    AnimatedHomeCardItem(
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
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            items(
                count = pagingItems.itemCount,
                key = { index ->
                    when (val item = pagingItems[index]) {
                        is Movie -> "movie_${item.id}"
                        is TvShow -> "tv_show_${item.id}"
                        else -> item?.hashCode()?.toString() ?: index.toString()
                    }
                }
            ) { index ->
                AnimatedPagingItem(
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
                    onNavigateToTvShow = onNavigateToTvShow
                )
            }
        }
    }
}

@Composable
private fun <T : Any> AnimatedHomeCardItem(
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
    var isVisible by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }

    LaunchedEffect(item) {
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible && !isDeleting,
        enter = slideInVertically(
            animationSpec = tween(500),
            initialOffsetY = { it }  // يبدأ من الأسفل
        ) + fadeIn(
            animationSpec = tween(500)
        ),
        exit = slideOutVertically(
            animationSpec = tween(500),
            targetOffsetY = { it }  // يختفي للأعلى
        ) + fadeOut(
            animationSpec = tween(500)
        )
    ) {
        HomeCard(
            imageUrl = imageUrl,
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
            onDeleteClick = {
                isDeleting = true
                onDeleteClick(item)
            },
            myRatingList = myRatingList,
            rate = rate
        )
    }
}

@Composable
private fun <T : Any> AnimatedPagingItem(
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
        imageUrl(item)?.let { url ->
            AnimatedHomeCardItem(
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
