package com.london.presentation.feature.accountinfo.rating

import com.london.domain.entity.RatedMedia

data class RatingData(
    val allRatedMedia: List<RatedMedia>,
    val ratedMovies: List<RatedMedia>,
    val ratedTvShows: List<RatedMedia>
)