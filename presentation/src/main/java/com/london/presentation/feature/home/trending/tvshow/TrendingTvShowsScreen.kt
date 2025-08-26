package com.london.presentation.feature.home.trending.tvshow

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.shared.BackgroundGradient
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.shared.container.MediaLazyVerticalGrid
import com.london.presentation.shared.genre.GenresSection
import com.london.presentation.utils.Listen
import com.london.presentation.utils.detailsTopBar

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
    Content(
        state = state,
        contract = viewModel,
    )

}

@Composable
private fun Content(
    state: TrendingTvShowsUiState,
    contract: TrendingTvShowsContract,
) {
    val screenWidth = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp }
    val tvShowsLazyItems = state.tvShowsFlow.collectAsLazyPagingItems()

    BuildScreen(
        isLoading = state.isLoading,
        isError = state.tvShowsFlow.collectAsLazyPagingItems().loadState.refresh is LoadState.Error,
        onBack = contract::onBackClick,
        emptyLayoutMessage = R.string.no_shows_in_genre,
        emptyLayoutImage = R.drawable.img_no_result,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            BackgroundGradient(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .zIndex(1f)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(NovixTheme.colors.surface)
            ) {
                TopBar(
                    modifier = Modifier.detailsTopBar(1f),
                    title = stringResource(R.string.trending_tv_shows),
                    onBackClick = contract::onBackClick
                )
                GenresSection(
                    genres = state.tvShowsGenres,
                    selectedGenre = state.selectedGenre,
                    screenWidth = screenWidth,
                    onGenreClick = contract::onGenreClick,
                    modifier = Modifier.padding(vertical = 12.dp),
                    getGenreName = { stringResource(it.stringResId) }
                )

                MediaLazyVerticalGrid(
                    pagingItems = tvShowsLazyItems,
                    imageUrl = { it.posterPath },
                    name = { it.title },
                    hasSaveIcon = false,
                    onItemClick = { item -> contract.onTvShowClick(item.id) },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }
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
