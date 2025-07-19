package com.london.presentation.features.details.actordetails.toptvshowspicks

import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails

data class TopTvShowsPicksUiState(
    val tvShowDetails: ActorTvShowDetails = ActorTvShowDetails(),
    val isSaved: Boolean = false,
    val backdropPath: String = "",
    val id: Int = 0,
)