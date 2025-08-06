package com.london.presentation.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.london.domain.entity.recent.MediaType
import com.london.presentation.R

@Composable
fun ContinueWatchingSection(
    recentWatchedMediaList: List<HomeUiMedia>,
    homeScreenContract: HomeScreenContract,
    modifier: Modifier = Modifier
) {
    HomeCarouselSection(
        modifier = modifier,
        uiMediaList = recentWatchedMediaList,
        sectionName = R.string.continue_watch,
        onSaveClick = {/*TODO: SAVE FUNCTIONALITY IS NOT IMPLEMENTED.*/ },
        onCardClick = { id, mediaType ->
            when (mediaType) {
                MediaType.TvShow -> homeScreenContract.onTvShowClick(id)
                MediaType.Movie -> homeScreenContract.onMovieClick(id)
            }
        },
        onAllClick = homeScreenContract::onContinueWatchingClick
    )
}