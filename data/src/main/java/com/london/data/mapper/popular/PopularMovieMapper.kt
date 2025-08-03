@file:KoverIgnore

package com.london.data.mapper.popular

import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.home.model.popular.PopularMovieResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.KoverIgnore
import com.london.domain.entity.popular.PopularMovie
import com.london.domain.entity.recent.MediaType

fun PopularMovieResponse.toPopularMovie(): PopularMovie {
    return PopularMovie(
        id = id.orZero(),
        title = title.orEmpty(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        rating = voteAverage.orZero().roundToDecimal()
    )
}

fun ApiResponse<PopularMovieResponse>.toPopularMovies(): List<PopularMovie> =
    items.map { it.toPopularMovie() }

fun PopularSectionLocal.toMovieEntity(): PopularMovie = PopularMovie(
    id = id,
    title = name,
    posterUrl = posterPictureUrl,
    rating = rating,
)

fun PopularMovie.toPopularMovieSectionLocal(
    mediaType: MediaType,
    date: Long = System.currentTimeMillis()
): PopularSectionLocal = PopularSectionLocal(
    id = id,
    name = title,
    posterPictureUrl = posterUrl,
    rating = rating,
    mediaType = mediaType,
    date = date
)
