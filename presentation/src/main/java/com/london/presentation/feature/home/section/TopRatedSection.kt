package com.london.presentation.feature.home.section

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.london.domain.entity.recent.MediaType
import com.london.presentation.R
import com.london.presentation.feature.home.HomeScreenContract
import com.london.presentation.feature.home.HomeScreenUiState

@Composable
fun TopRatedSection(
    uiState: HomeScreenUiState,
    homeScreenContract: HomeScreenContract,
    modifier: Modifier = Modifier
) {
    HomeCarouselSection(
        modifier = modifier,
        uiMediaList = uiState.topRatedMediaList,
        isLoading = uiState.isLoading,
        sectionName = R.string.top_rated,
        onSaveClick = { homeScreenContract.onManageBookmarkClick(it) },
        onCardClick = { id, mediaType ->
            when (mediaType) {
                MediaType.TvShow -> homeScreenContract.onTvShowClick(id)
                MediaType.Movie -> homeScreenContract.onMovieClick(id)
            }
        },
        onAllClick = homeScreenContract::onTopRatedClick
    )
}