package com.london.presentation.feature.reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.feature.reviews.components.ReviewsList
import com.london.presentation.feature.reviews.components.EmptyReviewsState
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.utils.Listen
import com.london.presentation.utils.isLoading
import com.london.designsystem.component.TopBar

@Composable
fun ReviewsScreen(
    viewModel: ReviewsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(initial = null)

    effect?.Listen {
        when (it) {
            is ReviewEffect.NavigateBack -> onNavigateBack()
        }
    }

    Content(
        uiState = uiState,
        reviewContract = viewModel,
    )
}

@Composable
private fun Content(
    uiState: ReviewsUiState,
    reviewContract: ReviewContract
) {
    val reviewsList = uiState.reviews.collectAsLazyPagingItems()

    BuildScreen(
        isLoading = reviewsList.isLoading(),
        isError = reviewsList.loadState.refresh is LoadState.Error,
        onBack = reviewContract::onBackClicked,
        onRetry = reviewContract::onRetry
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NovixTheme.colors.surface)
        ) {
            ReviewsTopBar(
                onBackClick = reviewContract::onBackClicked
            )

            if (reviewsList.itemSnapshotList.isEmpty()) {
                EmptyReviewsState(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
            } else {
                ReviewsList(
                    reviewsList = reviewsList,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun ReviewsTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopBar(
        title = stringResource(R.string.reviews),
        onBackClick = onBackClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = ReviewsDimens.HorizontalPadding)
            .padding(
                top = WindowInsets.statusBars.asPaddingValues()
                    .calculateTopPadding() + ReviewsDimens.TopBarTopPadding
            )
    )
}