package com.london.presentation.feature.list.viewitems

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R
import com.london.presentation.feature.list.bottomsheets.DeleteListBottomSheet
import com.london.presentation.shared.SnackBarAnimation
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.shared.container.MediaLazyVerticalGrid
import com.london.presentation.utils.Listen

@Composable
fun ViewItemsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMovieDetails: (Int) -> Unit,
    viewModel: ViewItemsViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            ViewItemsEffect.NavigateBack -> onNavigateBack()
            is ViewItemsEffect.NavigationMovieDetails ->
                onNavigateToMovieDetails(currentEffect.id)
        }
    }

    Content(
        state = state,
        contract = viewModel,
    )
}

@Composable
private fun Content(
    state: ViewItemsUiState,
    contract: ViewItemsContract,
) {
    val listItems = state.listItems.collectAsLazyPagingItems()
    Column {
        TopBar(
            title = state.listTitle,
            onBackClick = contract::onBackClick,
            option2Icon = R.drawable.ic_delete,
            onClickOption2 = contract::onDeleteClick,
            option2IconTint = NovixTheme.colors.redAccent,
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )

        BuildScreen(
            onBack = null,
            onRetry = listItems::refresh,
            isLoading = state.isLoading,
            isError = (state.error != null && state.error != ErrorState.RequestFailed()),
            emptyLayoutMessage = R.string.no_items_found,
            emptyLayoutImage = R.drawable.img_no_result,
            pagingFlow = listItems,
            handlePagingLoadingAutomatically = false
        ) {
            MediaLazyVerticalGrid(
                pagingItems = listItems,
                imageUrl = { it.posterUrl },
                name = { it.id.toString() },
                hasSaveIcon = true,
                onSaveClick = {
                    contract.onRemoveMovieClick(it.id)
                    contract.onRetryClick()
                },
                isItemSaved = { true },
                onNavigateToMovie = { id -> contract.onMovieClick(id) },
                onNavigateToTvShow = { }
            )
        }
    }

    DeleteListBottomSheet(
        isSheetVisible = state.isDeleteBottomSheetVisible,
        contract = contract,
    )

    SnackBarSection(state)
}

@Composable
private fun SnackBarSection(state: ViewItemsUiState) {
    when {
        state.error is ErrorState.RequestFailed -> {
            SnackBarAnimation(
                stringResource(R.string.list_deletion_failed)
            )
        }

        state.error is ErrorState.Timeout -> {
            SnackBarAnimation(stringResource(R.string.movie_not_found))
        }

        state.isSnackBarSuccessVisible -> {
            SnackBarAnimation(
                stringResource(R.string.movie_removed_successfully),
                icon = com.london.designsystem.R.drawable.ic_success
            )
        }
    }
}

@Composable
@Preview
@ThemePreviews
private fun Preview() {
    NovixTheme {
        Content(
            state = ViewItemsUiState(),
            contract = object : ViewItemsContract {
                override fun onBackClick() {}
                override fun onRetryClick() {}
                override fun onDeleteClick() {}
                override fun onConfirmDeleteClick() {}
                override fun onMovieClick(id: Int) {}
                override fun onRemoveMovieClick(id: Int) {}
                override fun onDeleteBottomSheetDismissClick() {}
            },
        )
    }
}
