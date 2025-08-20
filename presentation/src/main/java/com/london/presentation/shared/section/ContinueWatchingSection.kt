package com.london.presentation.shared.section

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.london.domain.entity.shared.MediaType
import com.london.presentation.R
import com.london.presentation.feature.home.HomeScreenContract
import com.london.presentation.feature.home.HomeUiMedia

@Composable
fun ContinueWatchingSection(
    homeScreenContract: HomeScreenContract,
    recentWatchedMediaList: List<HomeUiMedia>,
    modifier: Modifier = Modifier
) {
    HomeCarouselSection(
        modifier = modifier,
        uiMediaList = recentWatchedMediaList,
        sectionName = R.string.continue_watch,
        onSaveClick = { homeScreenContract.onManageBookmarkClicked(it) },
        onCardClick = { id, mediaType ->
            when (mediaType) {
                MediaType.TvShow -> homeScreenContract.onTvShowClick(id)
                MediaType.Movie -> homeScreenContract.onMovieClick(id)
            }
        },
        onAllClick = homeScreenContract::onContinueWatchingClick
    )
}
