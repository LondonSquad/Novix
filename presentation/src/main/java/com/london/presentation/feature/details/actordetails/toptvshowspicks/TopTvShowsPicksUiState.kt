package com.london.presentation.feature.details.actordetails.toptvshowspicks

import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails
import com.london.presentation.feature.base.ErrorState

data class TopTvShowsPicksUiState(
    val tvShowDetails: ActorTvShowDetails = ActorTvShowDetails(),
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val backdropPath: String = "",
    val id: Int = 0,
)