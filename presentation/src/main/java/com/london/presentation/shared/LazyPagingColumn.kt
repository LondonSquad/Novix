package com.london.presentation.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.EmptyLayout
import com.london.presentation.R
import com.london.presentation.feature.buildscreen.NetworkErrorScreen
import com.london.presentation.utils.isEmpty
import com.london.presentation.utils.isLoading

@Composable
fun <T : Any> LazyPagingColumn(
    emptyTitle: String,
    pagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit,
    onRetry: () -> Unit = {}
) {
    when {
        pagingItems.loadState.refresh is LoadState.Error -> NetworkErrorScreen(
            onRetry = onRetry, onBack = null
        )

        pagingItems.isLoading() -> CircularLoading(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )

        pagingItems.isLoading().not() && pagingItems.isEmpty() -> EmptyLayout(
            text = emptyTitle,
            image = R.drawable.img_no_result,
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        )

        else -> Content(
            modifier = modifier,
            items = pagingItems,
            itemContent = itemContent
        )
    }
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
