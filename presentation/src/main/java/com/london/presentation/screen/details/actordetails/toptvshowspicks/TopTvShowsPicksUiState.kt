package com.london.presentation.screen.details.actordetails.toptvshowspicks

import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails
import com.london.presentation.screen.base.ErrorState

data class TopTvShowsPicksUiState(
    val isLoading: Boolean = false,
    val error: ErrorState? = null,
    val tvShowDetails: ActorTvShowDetails = ActorTvShowDetails(),
    val isSaved: Boolean = false,
    val backdropPath: String = "",
    val id: Int = 0,
)