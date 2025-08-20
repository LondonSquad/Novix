package com.london.presentation.feature.list.viewitems

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.TopBar
import com.london.designsystem.snackbar.LocalSnackbarController
import com.london.designsystem.snackbar.SnackBarType
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.designsystem.utils.string
import com.london.presentation.R
import com.london.presentation.feature.list.bottomsheets.DeleteListBottomSheet
import com.london.presentation.shared.BackgroundGradient
import com.london.presentation.shared.MediaLazyPagingGrid
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

    Box(
        modifier = Modifier.fillMaxSize()
    ){

        BackgroundGradient(
            modifier = Modifier.align(Alignment.TopStart).zIndex(1f)
        )

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
                    hasSaveIcon = true
                )
            }
        }

        DeleteListBottomSheet(
            isSheetVisible = state.isDeleteBottomSheetVisible,
            contract = contract,
        )

        val snackBarController = LocalSnackbarController.current

        if (state.isSnackBarErrorVisible) {
            snackBarController.showMessage(
                message = R.string.deletion_failed.string,
                snackBarType = SnackBarType.Error,
                onComplete = contract::resetSnackBarErrorState,
                icon = null,
            )
        }

        if (state.isMovieSnackBarSuccessVisible) {
            snackBarController.showMessage(
                message = R.string.movie_removed_successfully.string,
                snackBarType = SnackBarType.Success,
                onComplete = contract::resetMovieSnackBarSuccessState,
                icon = com.london.designsystem.R.drawable.ic_success,
            )
        }

        if (state.isListSnackBarSuccess) {
            snackBarController.showMessage(
                message = R.string.list_removed_successfully.string,
                snackBarType = SnackBarType.Success,
                onComplete = contract::resetListSnackBarSuccessState,
                icon = com.london.designsystem.R.drawable.ic_success,
            )
        }
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
                override fun resetSnackBarErrorState() {}
                override fun resetMovieSnackBarSuccessState() {}
                override fun resetListSnackBarSuccessState() {}
            },
        )
    }
}
