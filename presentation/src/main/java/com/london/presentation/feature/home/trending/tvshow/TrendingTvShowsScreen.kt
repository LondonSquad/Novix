package com.london.presentation.feature.home.trending.tvshow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.shared.GenresSection
import com.london.presentation.shared.MediaLazyPagingGrid
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.utils.Listen

@Composable
fun TrendingTvShowsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTvShowDetails: (Int) -> Unit,
    viewModel: TrendingTvShowsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TrendingTvShowsEffect.TvShowDetailsNavigation -> onNavigateToTvShowDetails(
                currentEffect.tvShowId
            )

            is TrendingTvShowsEffect.BackNavigation -> onNavigateBack()
        }
    }

    BuildScreen(
        isLoading = state.isLoading,
        isError = state.tvShowsFlow.collectAsLazyPagingItems().loadState.refresh is LoadState.Error,
        onBack = viewModel::onBackClick,
        emptyLayoutMessage = R.string.no_trending_shows_in_genre,
        emptyLayoutImage = R.drawable.img_no_result,
    ) {
        Content(
            state = state,
            contract = viewModel,
        )
    }
}

@Composable
private fun Content(
    state: TrendingTvShowsUiState,
    contract: TrendingTvShowsContract,
) {
    val screenWidth = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp }
    val tvShowsLazyItems = state.tvShowsFlow.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NovixTheme.colors.surface)
    ) {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            title = stringResource(R.string.trending_tv_shows),
            onBackClick = contract::onBackClick
        )
        GenresSection(
            genres = state.tvShowsGenres,
            selectedGenre = state.selectedGenre,
            screenWidth = screenWidth,
            onGenreClick = contract::onGenreClick,
            modifier = Modifier.padding(bottom = 12.dp),
            getGenreName = { stringResource(it.stringResId) }
        )
        MediaLazyPagingGrid(
            pagingFlow = tvShowsLazyItems,
            onItemClick = { contract.onTvShowClick(it.id) },
            getImageUrl = { it.posterPath },
            getTitle = { it.title },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onSaveClick = { /* TODO: Implement save functionality */ },
            isItemSaved = { false },
        )
    }
}

@Preview
@Composable
private fun Preview() = NovixTheme {
    Content(
        state = TrendingTvShowsUiState(),
        contract = defaultTrendingTvShowsContract()
    )
}
