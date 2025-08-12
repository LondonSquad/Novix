package com.london.presentation.feature.details.actor.info.toptvshowspicks

import com.london.domain.entity.actordetails.cast.CastDetails
import com.london.presentation.shared.base.ErrorState

data class TopTvShowsPicksUiState(
    val id: Int = 0,
    val isSaved: Boolean = false,
    val backdropPath: String = "",
    val isLoading: Boolean = false,
    val errorState: ErrorState? = null,
    val actorTvShowDetails: CastDetails = CastDetails(),
)
