package com.london.presentation.screen.details.actordetails.toptvshowspicks

import com.london.domain.entity.actordetails.actortvshow.ActorTvShowDetails

data class TopTvShowsPicksUiState(
    val movieDetails: ActorTvShowDetails = ActorTvShowDetails(),
    val isSaved: Boolean = false,
    val backdropPath: String = "",
    val id: Int = 0,
    val numberOfMovies: Int = 0,
)