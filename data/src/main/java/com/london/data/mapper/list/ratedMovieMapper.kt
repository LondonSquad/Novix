package com.london.data.mapper.list

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.myrating.RatedMovieResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.myrating.RatedMovie


fun RatedMovieResponse.toRatedMovie(): RatedMovie {
    return RatedMovie(
        id = id.orZero(),
        posterPath = posterPath.asImageUrlOrEmpty(),
        rating = rating.orZero()
    )
}

fun ApiResponse<RatedMovieResponse>.toEntity(): List<RatedMovie> =
    items.map { it.toRatedMovie() }