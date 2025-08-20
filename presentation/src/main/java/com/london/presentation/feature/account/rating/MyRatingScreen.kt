package com.london.presentation.feature.account.rating

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.london.designsystem.component.NovixChip
import com.london.designsystem.component.TopBar
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.theme.ThemePreviews
import com.london.domain.entity.shared.MediaType
import com.london.presentation.R
import com.london.presentation.shared.BackgroundGradient
import com.london.presentation.shared.EmptyGenreLayout
import com.london.presentation.shared.SnackBarAnimation
import com.london.presentation.shared.base.ErrorState
import com.london.presentation.shared.buildscreen.BuildScreen
import com.london.presentation.shared.container.MediaLazyVerticalGrid
import com.london.presentation.utils.Listen
import com.london.presentation.utils.toLocalizedNumbers
import com.london.designsystem.R as dsR

@Composable
fun MyRatingScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMovieDetails: (Int) -> Unit,
    onNavigateToTvShowDetails: (Int) -> Unit,
    viewModel: MyRatingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    LaunchedEffect(Unit) {
        viewModel.initializeRatedMedia()
    }

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is MyRatingEffect.MovieDetailsNavigation -> onNavigateToMovieDetails(currentEffect.id)
            is MyRatingEffect.TvShowDetailsNavigation -> onNavigateToTvShowDetails(currentEffect.id)
            is MyRatingEffect.BackNavigation -> onNavigateBack()
        }
    }

    Content(
        state = state,
        contract = viewModel
    )
}

@Composable
private fun Content(
    state: MyRatingUiState = MyRatingUiState(),
    contract: MyRatingContract
) {
    val selectedCategory = state.selectedRatingCategory ?: RatingCategory.All
    val items = when (selectedCategory) {
        RatingCategory.All -> state.allRatedMedia
        RatingCategory.Movies -> state.ratedMovies
        RatingCategory.TvShows -> state.ratedTvShows
    }

    BuildScreen(
        isLoading = state.isLoading,
        isError = state.errorState is ErrorState.NoInternet,
        onBack = contract::onBackClick,
        onRetry = contract::onRetryClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            BackgroundGradient(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .zIndex(1f)
            )

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TopBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    title = stringResource(R.string.my_rating),
                    onBackClick = contract::onBackClick
                )

                RatingChipsRow(
                    selected = selectedCategory,
                    onSelect = contract::onRatingCategorySelected,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (items.isEmpty()) {
                    EmptyGenreLayout(
                        message = stringResource(R.string.there_is_no_items),
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    MediaLazyVerticalGrid(
                        items = items,
                        imageUrl = { it.posterPath },
                        name = { it.title },
                        hasSaveIcon = false,
                        rate = { rated -> rated.rating.toLocalizedNumbers() },
                        onDeleteClick = { rated ->
                            when (rated.mediaType) {
                                MediaType.Movie -> contract.onDeleteMovieClick(rated.id)
                                MediaType.TvShow -> contract.onDeleteTVShowClick(rated.id)
                            }
                        },
                        onItemClick = { rated ->
                            when (rated.mediaType) {
                                MediaType.Movie -> contract.onMovieClick(rated.id)
                                MediaType.TvShow -> contract.onTvShowClick(rated.id)
                            }
                        }
                    )
                }
            }

            RatingSnackBar(state)
        }
    }
}

@Composable
private fun RatingSnackBar(state: MyRatingUiState) {
    if (!state.isSnackBarVisible) return

    if (state.errorState is ErrorState.RequestFailed) {
        SnackBarAnimation(state.errorState.message)
    } else {
        SnackBarAnimation(
            stringResource(R.string.delete_list_successfully),
            dsR.drawable.ic_success
        )
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
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RatingCategory.entries.forEach { category ->
            NovixChip(
                text = stringResource(category.title),
                isSelected = selected == category,
                onClick = { onSelect(category) }
            )
        }
    }
}

@ThemePreviews
@Composable
private fun Preview() = NovixTheme {
    MyRatingScreen(
        onNavigateToMovieDetails = {},
        onNavigateToTvShowDetails = {},
        onNavigateBack = {}
    )
}
