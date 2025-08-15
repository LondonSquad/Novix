package com.london.presentation.feature.account.rating

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
    onNavigateMovie: (Int) -> Unit,
    onNavigateTvShow: (Int) -> Unit,
    viewModel: MyRatingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val effect by viewModel.effect.collectAsState(null)

    LaunchedEffect(Unit) {
        viewModel.initializeRatedMedia()
    }

    effect?.Listen { currentEffect ->
        when (currentEffect) {
            is MyRatingEffect.ToMovieNavigation -> onNavigateMovie(currentEffect.id)
            is MyRatingEffect.ToTvShowNavigation -> onNavigateTvShow(currentEffect.id)
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
    contract: MyRatingsContract
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
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                title = stringResource(R.string.my_rating),
                onBackClick = contract::onBackClick
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
                MediaLazyVerticalGrid(
                    items = items,
                    imageUrl = { it.posterPath },
                    name = { it.title },
                    rate = { it.rating.toLocalizedNumbers() },
                    hasSaveIcon = false,
                    isItemSaved = { false },
                    onSaveClick = {},
                    onDeleteClick = { rated ->
                        when (rated.mediaType) {
                            MediaType.Movie -> contract.onDeleteMovieClick(rated.id)
                            MediaType.TvShow -> contract.onDeleteTVShowClick(rated.id)
                        }
                    },
                    onNavigateToMovie = contract::onMovieClick,
                    onNavigateToTvShow = contract::onTvShowClick,
                )
            }
        }

        RatingSnackBar(state)
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
