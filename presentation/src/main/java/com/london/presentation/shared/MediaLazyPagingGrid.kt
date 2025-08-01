package com.london.presentation.shared

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.component.CircularLoading
import com.london.designsystem.component.EmptyLayout
import com.london.designsystem.component.HomeCard
import com.london.designsystem.utils.string
import com.london.presentation.R
import com.london.presentation.feature.buildscreen.NetworkErrorScreen
import com.london.presentation.utils.gridColmuns
import com.london.presentation.utils.isLoading

@Composable
fun <T : Any> MediaLazyPagingGrid(
    pagingFlow: LazyPagingItems<T>,
    onItemClick: (T) -> Unit,
    getImageUrl: (T) -> String,
    getTitle: (T) -> String,
    modifier: Modifier = Modifier,
    onSaveClick: (T) -> Unit = {},
    isItemSaved: (T) -> Boolean = { false },
    @StringRes noMediaMessage: Int,
    emptyImage: Int = R.drawable.img_no_result,
    onRetry: () -> Unit = {}
) {
    when {
        pagingFlow.loadState.refresh is LoadState.Error -> NetworkErrorScreen(
            onRetry = onRetry, onBack = null
        )

        pagingFlow.isLoading() -> CircularLoading(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        )

        pagingFlow.isLoading().not() && pagingFlow.itemCount == 0 -> EmptyLayout(
            text = noMediaMessage.string,
            image = emptyImage,
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        )

        else -> {
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
    }
}