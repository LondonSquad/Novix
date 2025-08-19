package com.london.data.mapper.home.popular

import com.london.data.local.model.home.popular.PopularSectionLocal
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.popular.PopularMovieResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.shared.MediaType

fun PopularMovieResponse.toPopularMovie(): PopularMedia = PopularMedia(
    id = id.orZero(),
    name = title.orEmpty(),
    posterUrl = posterPath.asImageUrlOrEmpty(),
    rating = voteAverage.orZero().roundToDecimal(),
    mediaType = MediaType.Movie
)


fun ApiResponse<PopularMovieResponse>.toPopularMovies(): List<PopularMedia> =
    items.map { it.toPopularMovie() }

fun PopularSectionLocal.toMovieEntity(): PopularMedia = PopularMedia(
    id = id,
    name = name,
    posterUrl = posterPictureUrl,
    rating = rating,
    mediaType = mediaType
)

fun PopularMedia.toPopularMovieSectionLocal(
    mediaType: MediaType,
    date: Long = System.currentTimeMillis()
): PopularSectionLocal = PopularSectionLocal(
    id = id,
    name = name,
    posterPictureUrl = posterUrl,
    rating = rating,
    mediaType = mediaType,
    date = date
)
