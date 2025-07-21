package com.london.data.mapper.popular

import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.home.popular.model.PopularMovieResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.popular.PopularMovie


fun PopularMovieResponse.toPopularMovie(): PopularMovie {
    return PopularMovie(
        id = this.id.orZero(),
        title = this.title.orEmpty(),
        posterPath = this.posterPath.asImageUrlOrEmpty(),
        backdropPath = this.backdropPath.asImageUrlOrEmpty(),
        voteAverage = this.voteAverage.orZero(),
    )
}

fun ApiResponse<PopularMovieResponse>.toPopularMovies(): List<PopularMovie> {
    return this.items.map { it.toPopularMovie() }
}