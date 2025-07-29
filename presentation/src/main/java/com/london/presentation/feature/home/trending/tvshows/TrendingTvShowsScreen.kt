package com.london.presentation.feature.home.trending.tvshows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.utils.string
import com.london.presentation.R
import com.london.presentation.shared.GenresSection
import com.london.presentation.shared.LazyPagingColumn
import com.london.presentation.shared.MediaLazyPagingGrid
import com.london.presentation.utils.Listen
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrendingTvShowsScreen(
    onTvShowClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: TrendingTvShowsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TrendingTvShowsEffect.NavigateToTvShow -> onTvShowClick(currentEffect.tvShowId)
            is TrendingTvShowsEffect.NavigateBack -> onBackClick()
        }
    }

    TrendingTvShowsContent(
        state = state,
        contract = viewModel,
    )
}


@Composable
private fun TrendingTvShowsContent(
    state: TrendingTvShowsUiState = TrendingTvShowsUiState(),
    contract: TrendingTvShowsContract = defaultTrendingTvShowsContract(),
) {
    val screenWidth = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp }
    val gridState = rememberLazyGridState()

    LaunchedEffect(state.selectedGenreId) {
        gridState.scrollToItem(0)
    }

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
            title = stringResource(R.string.trending_tv_shows),
            onBackClick = contract::onBack
        )

        GenresSection(
            genres = state.tvShowsGenres,
            selectedGenreId = state.selectedGenreId,
            screenWidth = screenWidth,
            onGenreClick = contract::onGenreSelected,
            modifier = Modifier.padding(bottom = 12.dp),
            getGenreId = { it.id },
            getGenreName = { stringResource(it.stringResId) }
        )

        val tvShowsLazyItems = state.tvShowsFlow.collectAsLazyPagingItems()

        LazyPagingColumn(
            emptyTitle = R.string.no_trending_tvshows_in_genre.string,
            pagingItems = tvShowsLazyItems,
        ) { tvShow ->
            MediaLazyPagingGrid(
                pagingFlow = tvShowsLazyItems,
                onItemClick = { contract.onTvShowClick(tvShow.id) },
                getImageUrl = { it.posterPath },
                getTitle = { it.title },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                onSaveClick = { /* TODO: Implement save functionality */ },
                isItemSaved = { false }
            )
        }
    }
}

@Preview
@Composable
private fun Preview() = NovixTheme {
    TrendingTvShowsContent()
}