@file:KoverIgnore

package com.london.data.mapper.popular

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.model.popular.PopularMovieResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.KoverIgnore
import com.london.domain.entity.popular.PopularMovie

fun PopularMovieResponse.toPopularMovie(): PopularMovie {
    return PopularMovie(
        id = id.orZero(),
        title = title.orEmpty(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        backdropUrl = backdropPath.asImageUrlOrEmpty(),
        rating = voteAverage.orZero().roundToDecimal()
    )
}

fun ApiResponse<PopularMovieResponse>.toPopularMovies(): List<PopularMovie> =
    items.map { it.toPopularMovie() }
