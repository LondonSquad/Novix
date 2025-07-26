package com.london.presentation.screen.home.trending.tvshow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.EmptyLayout
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.TopBar
import com.london.designsystem.component.NovixLoader
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.screen.home.trending.GenresSection
import com.london.presentation.utils.Listen
import com.london.presentation.utils.TvShowGenre
import org.koin.androidx.compose.koinViewModel
import com.london.designsystem.component.CircularLoading

@Composable
fun TrendingTvShowsScreen(
    viewModel: TrendingTvShowsViewModel = koinViewModel(),
    contract: TrendingTvShowsContract
) {
    val state = viewModel.state.collectAsState().value
    val effect = viewModel.effect.collectAsState().value
    val screenWidth = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp }

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TrendingTvShowsEffect.NavigateToTvShow -> {
                contract.onTvShowClick(currentEffect.tvShowId)
                viewModel.resetEffect()
            }

            TrendingTvShowsEffect.NavigateBack -> {
                contract.onBackClick()
                viewModel.resetEffect()
            }
        }
    }

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
            onBackClick = contract::onBackClick
        )
        GenresSection(
            genres = TvShowGenre.entries.toList(),
            selectedGenreId = state.selectedGenreId,
            screenWidth = screenWidth,
            onGenreClick = viewModel::onGenreSelected,
            modifier = Modifier.padding(bottom = 12.dp),
            getGenreId = { it.id },
            getGenreName = { stringResource(it.stringResId) }
        )

        val tvShowsLazyItems = state.trendingTvShows.collectAsLazyPagingItems()
        val isLoading = tvShowsLazyItems.loadState.refresh is androidx.paging.LoadState.Loading
        val filteredTvShows = List(tvShowsLazyItems.itemCount) { tvShowsLazyItems[it] }
            .filterNotNull()
            .filter { tvShow ->
                state.selectedGenreId == null ||
                        state.selectedGenreId == TvShowGenre.All.id ||
                        tvShow.genreIds.contains(state.selectedGenreId)
            }

        isLoading.takeIf { it }?.let {
            CircularLoading(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }

        (!isLoading).takeIf { it && filteredTvShows.isNotEmpty() }?.let {
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(filteredTvShows.size) { index ->
                    val tvShow = filteredTvShows[index]
                    HomeCard(
                        imageUrl = tvShow.posterPath,
                        isSaved = false,
                        onSaveClick = {},
                        modifier = Modifier.clickable { contract.onTvShowClick(tvShow.id) }
                    )
                }
            }
        }

        (!isLoading && filteredTvShows.isEmpty()).takeIf { it }?.let {
            EmptyLayout(
                text = stringResource(R.string.no_trending_tvshows_in_genre),
                image = R.drawable.img_no_result,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}
