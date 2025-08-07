package com.london.data.mapper.list

import com.london.data.remote.model.myrating.RatedMovieResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.myrating.RatedMovie


fun RatedMovieResponse.toEntity(): RatedMovie = RatedMovie(
    id = id.orZero(),
    posterPath = posterPath.asImageUrlOrEmpty(),
    rating = rating.orZero()
)