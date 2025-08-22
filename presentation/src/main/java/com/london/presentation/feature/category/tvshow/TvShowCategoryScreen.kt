package com.london.presentation.feature.category.tvshow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R
import com.london.designsystem.component.BackgroundGradient
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.shared.container.MediaLazyVerticalGrid
import com.london.presentation.utils.Listen
import com.london.presentation.utils.detailsTopBar
import com.london.presentation.utils.isLoading

@Composable
fun TvShowByCategoryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTvShowDetails: (Int) -> Unit,
    viewModel: TvShowCategoryViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            TvShowCategoryEffect.BackNavigation -> onNavigateBack()
            is TvShowCategoryEffect.TvShowDetailsNavigation -> onNavigateToTvShowDetails(
                currentEffect.tvShowId
            )
        }
    }

    Content(
        state = state,
        contract = viewModel,
    )
}

@Composable
private fun Content(
    state: TvShowCategoryUiState,
    contract: TvShowCategoryContract,
) {

    val tvShowLazyList = state.tvShowFlow.collectAsLazyPagingItems()
    BuildScreen(
        onBack = contract::onBackClick,
        isLoading = tvShowLazyList.isLoading(),
        isError = tvShowLazyList.loadState.refresh is LoadState.Error,
        onRetry = tvShowLazyList::refresh,
        emptyLayoutMessage = R.string.there_is_no_items_for_this_genre,
        emptyLayoutImage = R.drawable.empty
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            BackgroundGradient(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .zIndex(1f)
            )

            MediaLazyVerticalGrid(
                pagingItems = tvShowLazyList,
                imageUrl = { it.posterPicture },
                name = { it.name },
                hasSaveIcon = false,
                onNavigateToTvShow = { id -> contract.onTvShowClick(id) },
                topBar = {
                    TopBar(
                        modifier = Modifier
                            .detailsTopBar(1f)
                            .padding(bottom = 12.dp),
                        title = stringResource(state.genre.stringResId),
                        onBackClick = contract::onBackClick
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@ThemePreviews
@Composable
private fun Preview() {

    Content(
        state = TvShowCategoryUiState(),
        contract = object : TvShowCategoryContract {
            override fun onSavedClick(tvShowId: Int) {}
            override fun onTvShowClick(tvShowId: Int) {}
            override fun onBackClick() {}
        }
    )
}
