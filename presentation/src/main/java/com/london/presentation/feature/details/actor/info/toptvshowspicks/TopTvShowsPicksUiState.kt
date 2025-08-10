package com.london.presentation.feature.details.actor.info.toptvshowspicks

import com.london.domain.entity.actordetails.cast.CastDetails
import com.london.presentation.shared.base.ErrorState

data class TopTvShowsPicksUiState(
    val tvShowDetails: CastDetails = CastDetails(),
    val errorState: ErrorState? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val backdropPath: String = "",
    val id: Int = 0,
)