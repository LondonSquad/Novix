package com.london.presentation.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.london.domain.entity.recent.MediaType
import com.london.presentation.R

@Composable
fun TopRatedSection(
    uiState: HomeScreenUiState,
    homeScreenContract: HomeScreenContract,
    modifier: Modifier = Modifier
) {
    HomeCarouselSection(
        modifier = modifier,
        uiMediaList = uiState.topRatedUiMediaList,
        sectionName = R.string.top_rated,
        onSaveClick = {/*TODO: SAVE FUNCTIONALITY IS NOT IMPLEMENTED.*/ },
        onCardClick = { id, mediaType ->
            when (mediaType) {
                MediaType.TvShow -> homeScreenContract.onTvShowClick(id)
                MediaType.Movie -> homeScreenContract.onMovieClick(id)
            }
        },
        onAllClick = homeScreenContract::onTopRatedClick
    )
}