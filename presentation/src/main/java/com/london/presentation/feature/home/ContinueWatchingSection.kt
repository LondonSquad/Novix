package com.london.presentation.feature.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.london.designsystem.component.Text
import com.london.designsystem.theme.NovixTheme
import com.london.designsystem.utils.string
import com.london.domain.entity.recent.MediaType
import com.london.presentation.R

@Composable
fun ContinueWatchingSection(
    uiState: HomeScreenUiState,
    homeScreenContract: HomeScreenContract,
    modifier: Modifier = Modifier
) {
    HomeCarouselSection(
        modifier = modifier,
        uiMediaList = uiState.recentWatchedMediaList,
        sectionName = R.string.continue_watch,
        onSaveClick = {/*TODO: SAVE FUNCTIONALITY IS NOT IMPLEMENTED.*/ },
        onCardClick = { id, mediaType ->
            when (mediaType) {
                MediaType.TvShow -> homeScreenContract.onTvShowClick(id)
                MediaType.Movie -> homeScreenContract.onMovieClick(id)
            }
        },
        onAllClick = homeScreenContract::onContinueWatchingClick,
        emptyLayout = {
            Text(
                text = R.string.no_recent_history.string,
                style = NovixTheme.typography.body.small,
                color = NovixTheme.colors.body,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(24.dp)
            )
        }
    )
}