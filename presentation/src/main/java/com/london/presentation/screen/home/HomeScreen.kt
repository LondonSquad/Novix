package com.london.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.presentation.R
import com.london.presentation.screen.LoadingScreen
import com.london.presentation.screen.NetworkErrorScreen
import com.london.presentation.screen.base.ErrorState
import com.london.presentation.utils.Listen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    onMovieClick: (movieId: Int) -> Unit = {},
    viewModel: HomeViewModel = koinViewModel()
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is HomeScreenEffect.NavigationPopularCard -> onMovieClick(currentEffect.id)
        }
    }

    when {
        uiState.isLoading -> LoadingScreen()
        uiState.error == ErrorState.NoInternet -> NetworkErrorScreen()
        else -> Content(
            onMovieClick = { },
            onGenreClick = { },
            homeScreenContract = viewModel,
            uiState = uiState,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
private fun Content(
    onMovieClick: (movieId: Int) -> Unit,
    onGenreClick: (genreId: Int) -> Unit,
    homeScreenContract: HomeScreenContract,
    uiState: HomeScreenUiState,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val screenWidth = with(density) {
        LocalConfiguration.current.screenWidthDp.dp
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            top = 12.dp,
            bottom = 16.dp,
            start = 16.dp,
            end = 16.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .background(color = NovixTheme.colors.surface)
    ) {

        item(span = { GridItemSpan(maxLineSpan) }) {
            val pagerState =
                rememberPagerState(initialPage = 0, pageCount = { uiState.popularMovies.size })
            PopularSection(
                modifier = modifier
                    .requiredWidth(screenWidth),
                pagerState = pagerState,
                images = uiState.popularMovies.map { it.posterPath },
                onSaveClick = {/* TODO */},
                onCardClick = {
                    homeScreenContract.onPopularCardClicked(
                        uiState.popularMovies[pagerState.currentPage].id
                    )
                }
            )
        }

        upComingSection(
            onMovieClick = onMovieClick,
            onGenreClick = onGenreClick,
            screenWidth = screenWidth,
        )
    }
}

fun LazyGridScope.upComingSection(
    onMovieClick: (movieId: Int) -> Unit,
    onGenreClick: (genreId: Int) -> Unit,
    screenWidth: Dp,
) {
    item(span = { GridItemSpan(maxLineSpan) }) {
        Text(
            text = stringResource(R.string.upcoming),
            style = NovixTheme.typography.headline.small,
            color = NovixTheme.colors.title
        )
    }

    item(span = { GridItemSpan(maxLineSpan) }) {
        Spacer(modifier = Modifier.height(8.dp))
        GenresSection(
            onGenreClick = onGenreClick,
            screenWidth = screenWidth
        )
    }

    items(10) {
        HomeCard(
            imageUrl = "", // TODO
            isSaved = false,
            onSaveClick = { }, // TODO
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clickable { onMovieClick(it) }
        )
    }
}


@Composable
private fun GenresSection(
    onGenreClick: (genreId: Int) -> Unit,
    screenWidth: Dp
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.requiredWidth(screenWidth)
    ) {
        items(10) {
            NovixChip(
                text = "Adventure",//TODO()
                isSelected = true,
                onClick = { onGenreClick(it) }
            )
        }
    }
}