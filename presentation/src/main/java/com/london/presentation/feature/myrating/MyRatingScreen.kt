package com.london.presentation.feature.myrating

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.myrating.MediaItem
import com.london.domain.entity.myrating.RatedMovie
import com.london.domain.entity.myrating.RatedTvShow
import com.london.presentation.R
import com.london.presentation.shared.HomeCard
import com.london.presentation.shared.SnackBarAnimation
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.utils.Listen
import com.london.presentation.utils.gridColmuns
import com.london.designsystem.R as dsR

@Composable
fun MyRatingScreen(
    onNavigateMovie: (Int) -> Unit,
    onNavigateTvShow: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: MyRatingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    LaunchedEffect(Unit) {
        viewModel.refreshData()
    }

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is MyRatingEffect.NavigateToMovie -> onNavigateMovie(currentEffect.movieId)
            is MyRatingEffect.NavigateToTvShow -> onNavigateTvShow(currentEffect.tvShowId)
            is MyRatingEffect.NavigateBack -> onNavigateBack()
        }
    }

    BuildScreen(
        isLoading = state.isLoading,
        isError = state.errorState != null,
        onBack = viewModel::onBackClicked,
        onRetry = { viewModel.refreshData() },
        emptyLayoutMessage = R.string.no_rating_items_in_list,
        emptyLayoutImage = R.drawable.img_no_result
    ) {
        MyRatingContent(
            state = state,
            contract = viewModel
        )
    }
}

@Composable
private fun MyRatingContent(
    state: MyRatingUiState = MyRatingUiState(),
    contract: MyRatingContract = defaultMyRatingContract()
) {
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
            title = stringResource(R.string.my_rating),
            onBackClick = contract::onBackClicked
        )

        RatingChipsRow(
            selected = state.selectedRatingCategory ?: RatingCategory.All,
            onSelect = contract::onRatingCategorySelected,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        when (state.selectedRatingCategory) {
            RatingCategory.All -> {
                val allItems = state.allRated.movies.map { it.toMediaItem() } + 
                              state.allRated.tvShows.map { it.toMediaItem() }
                MediaGrid(
                    items = allItems,
                    onMovieClick = contract::onMovieClick,
                    onTvShowClick = contract::onTvShowClick
                )
            }
            RatingCategory.Movies -> {
                val movieItems = state.movies.map { it.toMediaItem() }
                MediaGrid(
                    items = movieItems,
                    onMovieClick = contract::onMovieClick
                )
            }
            RatingCategory.TvShows -> {
                val tvShowItems = state.tvShows.map { it.toMediaItem() }
                MediaGrid(
                    items = tvShowItems,
                    onTvShowClick = contract::onTvShowClick
                )
            }
            null -> {
                val allItems = state.allRated.movies.map { it.toMediaItem() } + 
                              state.allRated.tvShows.map { it.toMediaItem() }
                MediaGrid(
                    items = allItems,
                    onMovieClick = contract::onMovieClick,
                    onTvShowClick = contract::onTvShowClick
                )
            }
        }

        if (state.isDeleteClicked) {
            if (state.errorState is ErrorState.RequestFailed) {
                SnackBarAnimation(state.errorState.message)
            } else {
                SnackBarAnimation("Delete rating successfully", dsR.drawable.ic_success)
            }
        }
    }
}

@Composable
private fun MediaGrid(
    items: List<MediaItem>,
    onMovieClick: ((Int) -> Unit)? = null,
    onTvShowClick: ((Int) -> Unit)? = null
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(gridColmuns()),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            top = 12.dp,
            bottom = 16.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(items) { item ->
            HomeCard(
                imageUrl = item.posterPath,
                isSaved = false,
                onSaveClick = { },
                modifier = Modifier.clickable {
                    when {
                        item.isMovie && onMovieClick != null -> onMovieClick(item.id)
                        !item.isMovie && onTvShowClick != null -> onTvShowClick(item.id)
                    }
                }
            )
        }
    }
}

@Composable
private fun RatingChipsRow(
    selected: RatingCategory,
    onSelect: (RatingCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        RatingCategory.entries.forEach { category ->
            NovixChip(
                text = stringResource(category.title),
                isSelected = selected == category,
                onClick = {
                    if (selected != category) {
                        onSelect(category)
                    }
                }
            )
        }
    }
}

@ThemePreviews
@Composable
private fun Preview() = NovixTheme {
    MyRatingScreen(
        onNavigateMovie = {},
        onNavigateTvShow = {},
        onNavigateBack = {}
    )
}
