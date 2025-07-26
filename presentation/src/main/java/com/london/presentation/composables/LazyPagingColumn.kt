package com.london.presentation.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
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
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items.itemCount) { index ->
            val item = items.getOrNull(index)
            item?.let { itemContent(it) }
        }
    }
}
