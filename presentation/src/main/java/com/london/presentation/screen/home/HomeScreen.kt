package com.london.presentation.screen.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
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
import com.london.designsystem.theme.ThemePreviews
import com.london.presentation.R
import com.london.presentation.screen.LoadingScreen
import com.london.presentation.screen.NetworkErrorScreen
import com.london.presentation.screen.base.ErrorState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {

    val uiState by viewModel.state.collectAsStateWithLifecycle()
    Log.d("homeScreen", "movies: ${uiState.popularMovies}")
    Log.d("homeScreen", "error: ${uiState.error}")
    Log.d("homeScreen", "loading: ${uiState.isLoading}")

    when {
        uiState.isLoading -> LoadingScreen()
        uiState.error == ErrorState.NoInternet -> NetworkErrorScreen()
        else -> Content(
            onMovieClick = { },
            onGenreClick = { },
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp)
        )
    }
}

@Composable
private fun Content(
    onMovieClick: (movieId: Int) -> Unit,
    onGenreClick: (genreId: Int) -> Unit,
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
            Text(
                text = stringResource(R.string.upcoming),
                style = NovixTheme.typography.headline.small,
                color = NovixTheme.colors.title
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            GenresSection(
                onGenreClick = onGenreClick,
                screenWidth = screenWidth
            )
        }
        items(10) {
            HomeCard(
                imageUrl = "",//TODO()
                isSaved = false,
                onSaveClick = { },//TODO()
                modifier = Modifier.clickable { onMovieClick(it) })
        }
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

@ThemePreviews
@Composable
private fun Preview() {
    Content(
        onMovieClick = { },
        onGenreClick = { },
    )
}