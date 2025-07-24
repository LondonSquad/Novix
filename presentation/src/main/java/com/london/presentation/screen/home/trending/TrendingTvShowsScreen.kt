package com.london.presentation.screen.home.trending

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.screen.home.GenresSection
import com.london.presentation.utils.Listen
import org.koin.androidx.compose.koinViewModel
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun TrendingTvShowsScreen(
    modifier: Modifier = Modifier,
    viewModel: TrendingTvShowsViewModel = koinViewModel(),
    onTvShowClick: (Int) -> Unit,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val effect by viewModel.effect.collectAsState()
    val density = LocalDensity.current
    val screenWidth = with(density) { LocalConfiguration.current.screenWidthDp.dp }

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is TrendingTvShowsEffect.NavigateToTvShow -> {
                onTvShowClick(currentEffect.tvShowId)
                viewModel.resetEffect()
            }
            TrendingTvShowsEffect.NavigateBack -> {
                onBackClick()
                viewModel.resetEffect()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
    ) {
        TopBar(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            title = stringResource(R.string.trending_tv_shows),
            onBackClick = viewModel::onBackClick
        )
        GenresSection(
            genres = state.genres,
            selectedGenreId = state.selectedGenreId,
            screenWidth = screenWidth,
            onGenreClick = { viewModel.onGenreSelected(it) },
            modifier = Modifier.padding(bottom = 12.dp)
        )

        val tvShowsLazyItems = state.trendingTvShows.collectAsLazyPagingItems()
        val filteredTvShows = if (state.selectedGenreId == null || state.selectedGenreId == com.london.presentation.utils.Genre.All.id) {
            (0 until tvShowsLazyItems.itemCount).map { tvShowsLazyItems[it] }.filterNotNull()
        } else {
            (0 until tvShowsLazyItems.itemCount).map { tvShowsLazyItems[it] }.filterNotNull().filter { it.genreIds.contains(state.selectedGenreId) }
        }
        LazyVerticalGrid(
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
                    modifier = Modifier.clickable { viewModel.onTvShowClick(tvShow.id) }
                )
            }
        }
    }
} 