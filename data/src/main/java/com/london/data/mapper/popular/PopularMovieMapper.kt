@file:KoverIgnore

package com.london.data.mapper.popular

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.home.popular.model.PopularMovieResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.KoverIgnore
import com.london.domain.entity.popular.PopularMovie
import kotlin.math.round


fun PopularMovieResponse.toPopularMovie(): PopularMovie {
    return PopularMovie(
        id = id.orZero(),
        title = title.orEmpty(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        backdropUrl = backdropPath.asImageUrlOrEmpty(),
        rating = round(voteAverage.orZero() * 100) / 100.0
    )
}

fun ApiResponse<PopularMovieResponse>.toPopularMovies(): List<PopularMovie> {
    return items.map { it.toPopularMovie() }
}