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
import com.london.designsystem.component.HomeCard
import com.london.presentation.utils.gridColmuns

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
        columns = GridCells.Fixed(gridColmuns()),
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