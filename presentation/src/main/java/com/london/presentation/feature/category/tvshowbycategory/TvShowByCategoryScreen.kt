package com.london.presentation.feature.category.tvshowbycategory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.TvShow
import com.london.presentation.R
import com.london.presentation.feature.buildscreen.BuildScreen
import com.london.presentation.feature.search.SearchCategory
import com.london.presentation.shared.MediaLazyPagingGrid
import com.london.presentation.utils.Listen
import com.london.presentation.utils.convertGenreCodeToString
import org.koin.androidx.compose.koinViewModel

@Composable
fun TvShowByCategoryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTvShowDetails: (Int) -> Unit,
    viewModel: TvShowByCategoryViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            TvShowByCategoryEffect.NavigateBack -> onNavigateBack()
            is TvShowByCategoryEffect.NavigateToTvShowDetails -> onNavigateToTvShowDetails(
                currentEffect.tvShowId
            )
        }
    }
    BuildScreen(
        onBack = viewModel::onBack,
        isLoading = state.isLoading,
        isError = state.error != null
    ) {
             Content(
                state = state,
                contract = viewModel,
            )
    }
}

@Composable
private fun Content(
    state: TvShowByCategoryUiState,
    contract: TvShowByCategoryContract,
    modifier: Modifier = Modifier
) {

    val tvShowLazyList = state.tvShowFlow.collectAsLazyPagingItems()
    Column {
        TopBar(
            title = stringResource(
                convertGenreCodeToString(
                    genreId = state.categoryId, searchCategory = SearchCategory.TvShows
                )
            ), onBackClick = contract::onBack,
            modifier = modifier
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
        MediaLazyPagingGrid<TvShow>(
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
            emptyTitle = stringResource(id = R.string.no_tv_shows_found),
        )
    }
}

@ThemePreviews
@Composable
private fun Preview() {
    Content(
        state = TvShowByCategoryUiState(),
        contract = object : TvShowByCategoryContract {
            override fun onSavedClick(tvShowId: Int) {}

            override fun onTvShowClick(tvShowId: Int) {}

            override fun onBack() {}
        }
    )
}
