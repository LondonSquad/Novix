package com.london.presentation.feature.list.viewitems

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R
import com.london.presentation.feature.list.bottomsheets.DeleteListBottomSheet
import com.london.presentation.shared.MediaLazyPagingGrid
import com.london.presentation.shared.SnackBarAnimation
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.utils.Listen

@Composable
fun ViewListItemsScreen(
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
    contract: ViewListItemsContract,
) {
    val listItems = state.listItems.collectAsLazyPagingItems()
    Column {
        TopBar(
            title = state.listTitle,
            onBackClick = contract::onBack,
            option2Icon = R.drawable.ic_delete,
            onClickOption2 = contract::onDeleteClick,
            option2IconTint = NovixTheme.colors.redAccent,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )

        BuildScreen(
            onBack = null,
            onRetry = listItems::refresh,
            isLoading = state.isLoading,
            isError = state.error == ErrorState.NoInternet,
            emptyLayoutMessage = R.string.no_items_found,
            emptyLayoutImage = R.drawable.img_no_result,
            pagingFlow = listItems,
        ) {
            MediaLazyPagingGrid(
                pagingFlow = listItems,
                modifier = Modifier.padding(horizontal = 16.dp),
                onItemClick = { contract.onMovieClick(it.id) },
                getImageUrl = { it.posterUrl },
                getTitle = { "${it.id} media img" },
                onSaveClick = {
                    contract.onRemoveMovieClick(it.id)
                    contract.onRetry()
                },
                isItemSaved = { true },
            )
        }
    }

    DeleteListBottomSheet(
        isSheetVisible = state.isDeleteBottomSheetVisible,
        contract = contract,
    )

    if (state.isSnackBarErrorVisible) {
        SnackBarAnimation(
            stringResource(R.string.deletion_failed),
        )
    }

    if (state.isSnackBarSuccessVisible) {
        SnackBarAnimation(
            stringResource(R.string.movie_removed_successfully),
            icon = com.london.designsystem.R.drawable.ic_success
        )
    }
}

@Composable
@ThemePreviews
private fun Preview() {
    NovixTheme {
        Content(
            state = ViewItemsUiState(),
            contract = object : ViewListItemsContract {
                override fun onBack() {}
                override fun onRetry() {}
                override fun onDeleteClick() {}
                override fun onConfirmDelete() {}
                override fun onMovieClick(id: Int) {}
                override fun onRemoveMovieClick(id: Int) {}
                override fun onDeleteBottomSheetDismiss() {}
            },
        )
    }
}
