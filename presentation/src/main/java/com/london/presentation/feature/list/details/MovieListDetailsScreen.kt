package com.london.presentation.feature.list.details

import androidx.compose.foundation.layout.Box
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
import com.london.designsystem.component.BackgroundGradient
import com.london.designsystem.component.TopBar
import com.london.designsystem.snackbar.SnackBarData
import com.london.designsystem.snackbar.SnackBarType
import com.london.designsystem.snackbar.rememberSnackBarController
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.designsystem.utils.string
import com.london.presentation.R
import com.london.presentation.feature.list.bottomsheets.DeleteListBottomSheet
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.shared.container.MediaLazyVerticalGrid
import com.london.presentation.utils.Listen

@Composable
fun MovieListDetailsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMovieDetails: (Int) -> Unit,
    viewModel: MovieListDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            MovieListDetailsEffect.NavigateBack -> onNavigateBack()
            is MovieListDetailsEffect.NavigationMovieDetails ->
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
    state: MovieListDetailsUiState,
    contract: MovieListDetailsContract,
) {
    val listItems = state.listItems.collectAsLazyPagingItems()
    val snackBarController = rememberSnackBarController()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        BackgroundGradient(modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(1f)
        )

        BuildScreen(
            onBack = null,
            onRetry = listItems::refresh,
            isLoading = state.isLoading,
            isError = state.error == ErrorState.NoInternet,
            emptyLayoutMessage = R.string.list_empty,
            emptyLayoutImage = R.drawable.img_no_result,
            pagingFlow = listItems,
        ) {
            MediaLazyVerticalGrid(
                pagingItems = listItems,
                imageUrl = { it.posterUrl },
                name = { it.id.toString() },
                hasSaveIcon = true,
                onSaveClick = { contract.onRemoveMovieClick(it.id) },
                isItemSaved = { true },
                onNavigateToMovie = { id -> contract.onMovieClick(id) },
                topBar = {
                    TopBar(
                        title = state.listTitle,
                        onBackClick = contract::onBackClick,
                        option2Icon = R.drawable.ic_delete,
                        onClickOption2 = contract::onDeleteClick,
                        option2IconTint = NovixTheme.colors.redAccent,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    )
                }
            )
        }

        DeleteListBottomSheet(
            isSheetVisible = state.isDeleteBottomSheetVisible,
            contract = contract,
        )


        if (state.isSnackBarErrorVisible) {
            snackBarController.showSnackBar(
                SnackBarData(
                    message = R.string.item_delete_fail.string,
                    snackBarType = SnackBarType.Error,
                    onComplete = contract::resetSnackBarErrorState,
                )
            )
        }

        if (state.isMovieSnackBarSuccessVisible) {
            snackBarController.showSnackBar(
                SnackBarData(
                    message = R.string.item_delete_success.string,
                    snackBarType = SnackBarType.Success,
                    onComplete = contract::resetMovieSnackBarSuccessState,
                )
            )
        }

        if (state.isListSnackBarSuccess) {
            snackBarController.showSnackBar(
                SnackBarData(
                    message = R.string.list_delete_success.string,
                    snackBarType = SnackBarType.Success,
                    onComplete = contract::resetListSnackBarSuccessState,
                )
            )
        }
    }
}

@Composable
@ThemePreviews
private fun Preview() {
    NovixTheme {
        Content(
            state = MovieListDetailsUiState(),
            contract = object : MovieListDetailsContract {
                override fun onBackClick() {}
                override fun onRetryClick() {}
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
