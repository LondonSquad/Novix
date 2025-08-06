@file:KoverIgnore

package com.london.data.mapper.search

import com.london.data.local.model.home.upcoming.UpComingMovieLocal
import com.london.data.local.model.home.upcoming.UpComingSectionLocal
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.MovieRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.extractYear
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.Movie
import com.london.domain.entity.UpComingMovie

fun ApiResponse<MovieRemote>.toLocal(categoryId: Int?) = UpComingSectionLocal(
    date = System.currentTimeMillis(),
    page = currentPage,
    results = items.map { it.toUpComingLocal() },
    totalPages = totalPages,
    totalResults = totalItems,
    categoryId = categoryId?: 0
)

fun UpComingMovieLocal.toEntity() = UpComingMovie(
    id = id,
    imageUrl = imageUrl,
    genreIds = genreIds,
)

private fun MovieRemote.toUpComingLocal() = UpComingMovieLocal(
    id = id.orZero(),
    imageUrl = posterPath.asImageUrlOrEmpty(),
    genreIds = genreIds.orEmpty()
)

fun MovieRemote.toEntity() = Movie(
    id = id.orZero(),
    name = name.orEmpty(),
    posterUrl = posterPath.asImageUrlOrEmpty(),
    releaseYear = releaseDate?.extractYear().orZero(),
    rating = voteAverage?.toInt().orZero(),
    genreIds = genreIds.orEmpty()
)
