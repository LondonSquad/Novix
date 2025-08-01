package com.london.presentation.feature.buildscreen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.component.EmptyLayout


@Composable
fun BuildScreen(
    isLoading: Boolean = false,
    isError: Boolean = false,
    onBack: () -> Unit,
    onRetry: () -> Unit = {},
    @StringRes emptyLayoutMessage: Int? = null,
    @DrawableRes emptyLayoutImage: Int? = null,
    pagingFlow: LazyPagingItems<*>? = null,
    handlePagingLoadingAutomatically: Boolean = true,
    content: @Composable () -> Unit,
) {
    when {
        isLoading || (handlePagingLoadingAutomatically && pagingFlow?.loadState?.refresh is LoadState.Loading) -> {
            LoadingScreen()
        }
        isError || (pagingFlow?.loadState?.refresh is LoadState.Error) -> {
            NetworkErrorScreen(onBack = onBack, onRetry = onRetry)
        }
        pagingFlow != null &&
                pagingFlow.itemCount == 0 &&
                pagingFlow.loadState.refresh is LoadState.NotLoading &&
                emptyLayoutMessage != null -> {
            EmptyLayout(
                text = stringResource(emptyLayoutMessage),
                image = emptyLayoutImage ?: 0
            )
        }
        else -> {
            content()
        }
    }
}