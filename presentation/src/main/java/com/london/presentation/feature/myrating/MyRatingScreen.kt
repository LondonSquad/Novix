package com.london.presentation.feature.myrating

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R
import com.london.presentation.shared.GenresSection
import com.london.presentation.shared.MediaLazyPagingGrid
import com.london.presentation.shared.SnackBarAnimation
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.utils.Listen
import com.london.presentation.utils.isLoading
import com.london.designsystem.R as dsR

@Composable
fun MyRatingScreen(
    onNavigateMovie: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: MyRatingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is MyRatingEffect.NavigateToItem -> onNavigateMovie(currentEffect.itemId)
            is MyRatingEffect.NavigateBack -> onNavigateBack()
        }
    }


    val lazyItems = state.itemsFlow.collectAsLazyPagingItems()

    BuildScreen(
        isLoading = lazyItems.isLoading(),
        isError = lazyItems.loadState.refresh is LoadState.Error,
        onBack = viewModel::onBackClicked,
        onRetry = viewModel::onDelete,
        emptyLayoutMessage = R.string.no_rating_items_in_list,
        emptyLayoutImage = R.drawable.img_no_result,
        pagingFlow = lazyItems
    ) {
        MyRatingContent(
            state = state,
            contract = viewModel
        )
    }

}

@Composable
private fun MyRatingContent(
    state: MyRatingUiState = MyRatingUiState(),
    contract: MyRatingContract = defaultMyRatingContract()
) {
    val screenWidth = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
    ) {
        TopBar(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            title = stringResource(R.string.my_rating),
            onBackClick = contract::onBackClicked
        )
        GenresSection(
            genres = state.genres,
            selectedGenreId = state.selectedGenreId,
            screenWidth = screenWidth,
            onGenreClick = contract::onGenreSelected,
            modifier = Modifier.padding(bottom = 12.dp),
            getGenreId = { it.id },
            getGenreName = { stringResource(it.stringResId) }
        )
        MediaLazyPagingGrid(
            pagingFlow = state.itemsFlow.collectAsLazyPagingItems(),
            onItemClick = { contract.onItemClick(it.id) },
            getImageUrl = { it.posterPath },
            getTitle = { it.title },
            myRatingList = true,
            rate = rate,
            onDeleteClick = { /* TODO: Implement delete functionality */ },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
        if (state.isDeleteClicked) {
            if (state.errorState is ErrorState.RequestFailed) {
                val message = state.errorState.message
                SnackBarAnimation(message)
            }
            else {
                val message = "Delete rating successfully"
                SnackBarAnimation(message, dsR.drawable.ic_success)
            }
        }
    }
}

@ThemePreviews
@Composable
private fun MyRatingScreenPreview() = NovixTheme {
    MyRatingScreen(
        onNavigateMovie = {},
        onNavigateBack = {}
    )
}