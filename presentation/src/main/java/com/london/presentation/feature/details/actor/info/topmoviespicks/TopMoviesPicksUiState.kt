package com.london.presentation.feature.details.actor.info.topmoviespicks

import com.london.domain.entity.actor.ActorMediaDetails
import com.london.presentation.shared.base.ErrorState

data class TopMoviesPicksUiState(
    val isLoading: Boolean = false,
    val errorState: ErrorState? = null,
    val movieDetails: ActorMediaDetails = ActorMediaDetails(),
    val isBookmarkSheetVisible: Boolean = false,
    val bookmarkedMovieId: Int = 0,
)
