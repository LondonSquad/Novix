package com.london.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.component.HomeCard

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
    val screenWidth = LocalWindowInfo.current.containerSize.width
    val itemWidthPx = with(LocalDensity.current) { 158.dp.toPx() }
    val screenPaddingPx = with(LocalDensity.current) { 32.dp.toPx() }
    val columns = ((screenWidth - screenPaddingPx) / itemWidthPx).toInt().coerceAtLeast(2)
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(columns),
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
                    modifier = Modifier.clickable { onItemClick(item) }
                )
            }
        }
    }
}
