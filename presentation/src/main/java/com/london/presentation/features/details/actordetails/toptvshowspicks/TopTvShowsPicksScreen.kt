package com.london.presentation.features.details.actordetails.toptvshowspicks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun TopTvShowsPicksScreen(
    modifier: Modifier = Modifier,
    viewModel: TopTvShowsPicksViewModel = koinViewModel(),
    onTvShowClick: (movieId: Int) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    TopTvShowsPicksContent(
        state = state,
        interactions = viewModel,
        modifier = modifier,
        onBackClick = onBackClick,
        onTvShowClick = onTvShowClick,
    )
}

@Composable
private fun TopTvShowsPicksContent(
    state: TopTvShowsPicksUiState,
    interactions: TopTvShowsPicksInteractions,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onTvShowClick: (movieId: Int) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(top = 12.dp, bottom = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
            .padding(horizontal = 16.dp)
    ) {
        item(
            span = { GridItemSpan(maxLineSpan) }) {
            TopBar(
                modifier = Modifier.statusBarsPadding(),
                title = stringResource(R.string.top_tv_shows_picks),
                onBackClick = onBackClick
            )
        }
        items(state.tvShowDetails.cast) { tvShow ->
            HomeCard(
                imageUrl = tvShow.posterUrl,
                isSaved = false,
                onSaveClick = { interactions.onSaveMovie(tvShow.id) },
                modifier = Modifier.clickable{ onTvShowClick(tvShow.id) }
            )
        }
    }
}