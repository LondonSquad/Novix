package com.london.presentation.screen.toprated

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.london.designsystem.component.HomeCard
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.Text
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TopRatedScreen(
    viewModel: TopRatedViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onGenreClick: (Int) -> Unit = {},
    onMovieClick: (Int) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TopRatedScreenContent(
        state = state,
        modifier = modifier,
        onBackClick = onBackClick,
        onGenreClick = onGenreClick,
        onMovieClick = onMovieClick
    )
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun TopRatedScreenContent(
    state: TopRatedUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onGenreClick: (Int) -> Unit,
    onMovieClick: (Int) -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = NovixTheme.colors.surface)
            .padding(WindowInsets.navigationBars.asPaddingValues())
    ) {
        TopBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            title = "",
            onBackClick = onBackClick
        )

        state.genre?.let { genres ->
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier
                    .requiredWidth(screenWidth)
                    .padding(vertical = 12.dp)
            ) {
                items(genres) { genre ->
                    NovixChip(
                        text = genre.genreName,
                        isSelected = genre.isSelected,
                        onClick = { onGenreClick(genre.genreId) }
                    )
                }
            }
        }

        when (val mediaState = state.media) {
            is MediaUiState.Combined -> {
                val moviesPagingItems = mediaState.movies.collectAsLazyPagingItems()
                val tvSeriesPagingItems = mediaState.tvSeries.collectAsLazyPagingItems()


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
                    modifier = Modifier.background(NovixTheme.colors.surface)
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = "",
                            style = NovixTheme.typography.headline.small,
                            color = NovixTheme.colors.title
                        )
                    }

                    val maxItems = maxOf(moviesPagingItems.itemCount, tvSeriesPagingItems.itemCount)
                    items(count = maxItems * 2) { index ->
                        when {
                            index % 2 == 0 -> {
                                val movieIndex = index / 2
                                if (movieIndex < moviesPagingItems.itemCount) {
                                    val movie = moviesPagingItems[movieIndex]
                                    movie?.let { movieItem ->
                                        HomeCard(
                                            imageUrl = movieItem.posterUrl,
                                            isSaved = false,
                                            onSaveClick = {},
                                            modifier = Modifier.clickable {
                                                onMovieClick(movieItem.id)
                                            }
                                        )
                                    }
                                }
                            }

                            else -> {
                                val tvSeriesIndex = index / 2
                                if (tvSeriesIndex < tvSeriesPagingItems.itemCount) {
                                    val tvSeries = tvSeriesPagingItems[tvSeriesIndex]
                                    tvSeries?.let { seriesItem ->
                                        HomeCard(
                                            imageUrl = seriesItem.posterUrl,
                                            isSaved = false,
                                            onSaveClick = {

                                            },
                                            modifier = Modifier.clickable {
                                                onMovieClick(seriesItem.id)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            MediaUiState.Empty -> {}
        }
    }
}