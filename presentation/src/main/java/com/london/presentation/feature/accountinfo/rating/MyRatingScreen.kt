package com.london.presentation.feature.accountinfo.rating

import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import com.london.domain.entity.recent.MediaType
import com.london.presentation.R
import com.london.presentation.shared.EmptyGenreLayout
import com.london.presentation.shared.HomeCard
import com.london.presentation.shared.SnackBarAnimation
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.utils.Listen
import com.london.presentation.utils.gridColumns
import com.london.presentation.utils.toLocalizedNumbers
import com.london.designsystem.R as dsR

@Composable
fun MyRatingScreen(
    onNavigateBack: () -> Unit,
    onNavigateMovie: (Int) -> Unit,
    onNavigateTvShow: (Int) -> Unit,
    viewModel: RatingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    LaunchedEffect(Unit) {
        viewModel.initializeItems()
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
        onRetry = {
            viewModel.initializeItems()
        }
    ) {
        Content(
            state = state,
            contract = viewModel
        )
    }
}

@Composable
private fun Content(
    state: MyRatingUiState = MyRatingUiState(),
    contract: MyRatingContract = defaultMyRatingContract()
) {
    val selectedCategory = state.selectedRatingCategory ?: RatingCategory.All
    val items = when (selectedCategory) {
        RatingCategory.All -> state.allRatedMedia
        RatingCategory.Movies -> state.ratedMovies
        RatingCategory.TvShows -> state.ratedTvShows
    }

    Column(
        modifier = Modifier.fillMaxSize()
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

        if (items.isEmpty()) {
            EmptyGenreLayout(
                message = stringResource(R.string.there_is_no_items),
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridColumns()),
                contentPadding = PaddingValues(
                    top = 12.dp,
                    bottom = 16.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp)
            ) {
                items(
                    items = items,
                    key = { it.id }
                ) { item ->
                        HomeCard(
                            imageUrl = item.posterPath,
                            isSaved = false,
                            onSaveClick = { },
                            myRatingList = true,
                            rate = item.rating.toLocalizedNumbers(),
                            onDeleteClick = {
                                contract.onDelete(
                                    id = item.id,
                                    mediaType = item.mediaType
                                )
                            },
                            modifier = Modifier.animateItem(
                                fadeInSpec = null,
                                fadeOutSpec = tween(500),
                                placementSpec = tween(500)
                            ).clickable {
                                when (item.mediaType) {
                                    MediaType.Movie -> contract.onMovieClick(item.id)
                                    MediaType.TvShow -> contract.onTvShowClick(item.id)
                                }
                            },
                            isDarkMode = NovixTheme.isThemeDark
                        )
                    }
            }
        }
    }

    if (state.isSnackBarVisible) {
        if (state.errorState is ErrorState.RequestFailed) {
            SnackBarAnimation(state.errorState.message)
        } else {
            SnackBarAnimation(
                stringResource(R.string.delete_list_successfully),
                dsR.drawable.ic_success
            )
        }
    }
}

@Composable
fun RatingChipsRow(
    selected: RatingCategory,
    onSelect: (RatingCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NovixChip(
            text = stringResource(R.string.all),
            isSelected = selected == RatingCategory.All,
            onClick = { onSelect(RatingCategory.All) }
        )
        NovixChip(
            text = stringResource(R.string.Movies),
            isSelected = selected == RatingCategory.Movies,
            onClick = { onSelect(RatingCategory.Movies) }
        )
        NovixChip(
            text = stringResource(R.string.TV_Shows),
            isSelected = selected == RatingCategory.TvShows,
            onClick = { onSelect(RatingCategory.TvShows) }
        )
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
