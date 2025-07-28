package com.london.presentation.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.EmptyLayout
import com.london.presentation.R
import com.london.presentation.utils.getOrNull
import com.london.presentation.utils.isEmpty
import com.london.presentation.utils.isLoading
import kotlinx.coroutines.flow.Flow


@Composable
fun<T: Any> LazyPagingColumn(
    emptyTitle: String,
    pagingFlow: Flow<PagingData<T>>,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit
) {
    val pagingData = pagingFlow.collectAsLazyPagingItems()

    when {
        pagingData.isLoading() -> CircularLoading(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )
        pagingData.isLoading().not() && pagingData.isEmpty() -> EmptyLayout(
            text = emptyTitle,
            image = R.drawable.img_no_result,
        )
        else -> Content(
            modifier = modifier,
            items = pagingData,
            itemContent = itemContent
        )
    }
}

@Composable
private fun <T : Any> Content(
    modifier: Modifier,
    items: LazyPagingItems<T>,
    itemContent: @Composable ((T) -> Unit)
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(items.itemCount) { index ->
            val item = items.getOrNull(index)
            item?.let { itemContent(it) }
        }
    }
}
