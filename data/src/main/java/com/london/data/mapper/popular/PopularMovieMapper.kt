package com.london.data.mapper.popular

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.home.popular.model.PopularMovieResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.popular.PopularMovie


fun PopularMovieResponse.toPopularMovie(): PopularMovie {
    return PopularMovie(
        id = id.orZero(),
        title = title.orEmpty(),
        posterPath = posterPath.asImageUrlOrEmpty(),
        backdropPath = backdropPath.asImageUrlOrEmpty(),
        voteAverage = voteAverage.orZero(),
    )
}

fun ApiResponse<PopularMovieResponse>.toPopularMovies(): List<PopularMovie> {
    return items.map { it.toPopularMovie() }
}