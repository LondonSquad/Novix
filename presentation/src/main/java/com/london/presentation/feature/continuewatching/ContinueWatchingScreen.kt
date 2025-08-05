package com.london.presentation.feature.continuewatching

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.R
import com.london.presentation.shared.HomeCard
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.TabItem
import com.london.designsystem.component.TabLayout
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R.string
import com.london.presentation.shared.EmptyStateView
import com.london.presentation.utils.Listen
import com.london.presentation.utils.MovieGenre
import com.london.presentation.utils.TvShowGenre
import com.london.presentation.utils.gridColmuns

@Composable
fun ContinueWatchingScreen(
    viewModel: ContinueWatchingViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onMovieClick: (Int) -> Unit = {},
    onTvShowClick: (Int) -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen {
        when (it) {
            is ContinueWatchingEffect.NavigateToMovieDetails -> onMovieClick(it.id)
            is ContinueWatchingEffect.NavigateToTvShowDetails -> onTvShowClick(it.id)
            is ContinueWatchingEffect.NavigateBack -> onBackClick()
        }
    }

    Content(
        state = state,
        continueWatchingContract = viewModel
    )
}

@Composable
fun Content(
    state: ContinueWatchingUiState = ContinueWatchingUiState(),
    continueWatchingContract: ContinueWatchingContract = defaultContinueWatchingContract()
) {

    val screenWidth =
        with(LocalDensity.current) { LocalWindowInfo.current.containerSize.width.toDp() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(WindowInsets.navigationBars.asPaddingValues())

    ) {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp),
            title = stringResource(string.continue_watch),
            onBackClick = continueWatchingContract::onBack
        )

        TabLayout(
            tabs = listOf(
                TabItem(R.string.movies),
                TabItem(R.string.tv_shows),
            ),
            selectedIndex = state.tabSelected,
            onTabSelected = continueWatchingContract::tabSelected,
            modifier = Modifier.background(NovixTheme.colors.surface)
        )
        when {
            state.isMovieSelected -> MovieGenreRow(
                onGenreClick = continueWatchingContract::movieGenre,
                state = state,
                screenWidth = screenWidth
            )

            state.isTvSelected -> TvShowRow(
                onGenreClick = continueWatchingContract::tvShowGenre,
                state = state,
                screenWidth = screenWidth
            )
            else -> EmptyStateView()
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(gridColmuns()),
            contentPadding = PaddingValues(
                top = 12.dp,
                bottom = 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.background(NovixTheme.colors.surface)
        ) {

            if (state.isMovieSelected) {
                items(state.movies.size) { index ->
                    val movie = state.movies[index]
                    movie.let { movieItem ->
                        HomeCard(
                            imageUrl = movieItem.posterUrl,
                            isSaved = false,
                            onSaveClick = {
                                // TODO
                            },
                            modifier = Modifier.clickable {
                                continueWatchingContract.onNavigateToMovie(movieItem.id)
                            }
                        )
                    }
                }
            }
            items(state.tvSeries.size) { index ->
                val tvSeries = state.tvSeries[index]
                tvSeries.let { seriesItem ->
                    HomeCard(
                        imageUrl = seriesItem.posterPicture,
                        isSaved = false,
                        onSaveClick = {
                            // TODO
                        },
                        modifier = Modifier.clickable {
                            continueWatchingContract.onNavigateToTvShow(seriesItem.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MovieGenreRow(
    onGenreClick: (MovieGenre) -> Unit,
    state: ContinueWatchingUiState,
    screenWidth: Dp,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier
            .requiredWidth(screenWidth)
            .padding(vertical = 12.dp)
    ) {
        items(MovieGenre.entries.toTypedArray()) { genre ->
            NovixChip(
                text = stringResource(genre.stringResId),
                isSelected = genre == state.selectedMovieGenre,
                onClick = { onGenreClick(genre) })
        }
    }
}

@Composable
private fun TvShowRow(
    onGenreClick: (TvShowGenre) -> Unit,
    state: ContinueWatchingUiState,
    screenWidth: Dp,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier
            .requiredWidth(screenWidth)
            .padding(vertical = 12.dp)
    ) {
        items(TvShowGenre.entries.toTypedArray()) { genre ->
            NovixChip(
                text = stringResource(genre.stringResId),
                isSelected = genre == state.selectedTvShowGenre,
                onClick = { onGenreClick(genre) })
        }
    }
}