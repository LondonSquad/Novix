package com.london.presentation.feature.buildscreen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.london.designsystem.component.EmptyLayout
import com.london.presentation.utils.isEmpty
import com.london.presentation.utils.isNotNull
import com.london.presentation.utils.shouldShowLoading


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
        shouldShowLoading(
            isLoading = isLoading,
            handlePagingLoadingAutomatically = handlePagingLoadingAutomatically,
            pagingFlow = pagingFlow
        ) -> { LoadingScreen() }

        isError || (pagingFlow?.loadState?.refresh is LoadState.Error) -> {
            NetworkErrorScreen(onBack = onBack, onRetry = onRetry)
        }

        (pagingFlow!=null)  &&
                pagingFlow.isEmpty() &&
                emptyLayoutMessage.isNotNull() -> {
            EmptyLayout(
                text = stringResource(emptyLayoutMessage!!),
                image = emptyLayoutImage ?: 0
            )
        }

        else -> {
            content()
        }
    }
}

@Composable
fun BuildScreen(
    isLoading: Boolean = false,
    isError: Boolean = false,
    isGuest: Boolean = false,
    onBack: () -> Unit = {},
    onRetry: () -> Unit = {},
    @StringRes emptyLayoutMessage: Int? = null,
    @DrawableRes emptyLayoutImage: Int? = null,
    pagingFlow: LazyPagingItems<*>? = null,
    handlePagingLoadingAutomatically: Boolean = false,
    emptyContent: (@Composable () -> Unit)? = null,
    guestContent: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    when {
        shouldShowLoading(
            isLoading = isLoading,
            handlePagingLoadingAutomatically = handlePagingLoadingAutomatically,
            pagingFlow = pagingFlow
        ) -> { LoadingScreen() }

        isError || (pagingFlow?.loadState?.refresh is LoadState.Error) -> {
            NetworkErrorScreen(onBack = onBack, onRetry = onRetry)
        }

        isGuest && guestContent != null  -> { guestContent.invoke() }

        (pagingFlow!=null)  &&
                pagingFlow.isEmpty() &&
                emptyLayoutMessage.isNotNull() -> {
            EmptyLayout(
                text = stringResource(emptyLayoutMessage!!),
                image = emptyLayoutImage ?: 0
            )
        }
        pagingFlow != null &&
                pagingFlow.isEmpty() &&
                emptyContent != null -> emptyContent()
        else -> {
            content()
        }
    }
}