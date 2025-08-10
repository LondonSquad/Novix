package com.london.presentation.feature.accountinfo.rating

import com.london.domain.entity.RatedMedia

data class RatingData(
    val ratedMovies: List<RatedMedia>,
    val ratedTvShows: List<RatedMedia>
)