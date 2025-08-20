package com.london.presentation.feature.category.tvshow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.london.presentation.shared.BackgroundGradient
import com.london.presentation.shared.MediaLazyPagingGrid
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.utils.Listen
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
        onBack = contract::onBack,
        isLoading = tvShowLazyList.isLoading(),
        isError = tvShowLazyList.loadState.refresh is LoadState.Error,
        onRetry = tvShowLazyList::refresh,
        emptyLayoutMessage = R.string.there_is_no_items_for_this_genre,
        emptyLayoutImage = R.drawable.empty
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
        ){
            BackgroundGradient(
                modifier = Modifier.align(Alignment.TopStart).zIndex(1f)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp)
            ) {
                TopBar(
                    title = stringResource(
                        state.genre.stringResId
                    ),
                    onBackClick = contract::onBack,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                )

                MediaLazyPagingGrid(
                    pagingFlow = tvShowLazyList,
                    onItemClick = { contract.onTvShowClick(it.id) },
                    getImageUrl = { it.posterPicture },
                    getTitle = { "${it.name} tv show img" },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onSaveClick = { /* TODO: Implement save functionality */ },
                    isItemSaved = { false },
                )
            }
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
            override fun onBack() {}
        }
    )
}
