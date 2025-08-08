package com.london.presentation.feature.myrating

import com.london.domain.entity.myrating.RatedMedia

data class RatingData(
    val allRatedMedia: List<RatedMedia>,
    val ratedMovies: List<RatedMedia>,
    val ratedTvShows: List<RatedMedia>
)