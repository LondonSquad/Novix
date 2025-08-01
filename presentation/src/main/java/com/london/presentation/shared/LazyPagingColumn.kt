package com.london.presentation.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems

@Composable
fun <T : Any> LazyPagingColumn(
    emptyTitle: String,
    pagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit,
    onRetry: () -> Unit = {}
) {
    Content(
            modifier = modifier,
            items = pagingItems,
            itemContent = itemContent
        )

}

@Composable
private fun <T : Any> Content(
    modifier: Modifier,
    items: LazyPagingItems<T>,
    itemContent: @Composable (T) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.itemSnapshotList.forEach { item ->
            if (item != null) {
                itemContent(item)
            }
        }
    }
}
