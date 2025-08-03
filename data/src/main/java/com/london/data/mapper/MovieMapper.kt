@file:KoverIgnore

package com.london.data.mapper

import com.london.data.local.model.home.upcoming.UpComingMovieDtoLocal
import com.london.data.local.model.home.upcoming.UpComingSectionLocal
import com.london.data.local.model.search.SearchMovieDtoLocal
import com.london.data.local.model.search.SearchMoviesLocal
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.search.model.MovieRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.extractYear
import com.london.data.utils.generateHash
import com.london.data.utils.orZero
import com.london.domain.KoverIgnore
import com.london.domain.entity.Movie
import com.london.domain.entity.UpComingMovie

fun SearchMovieDtoLocal.toEntity() = Movie(
    id = id,
    posterUrl = posterPath.asImageUrlOrEmpty(),
    genreIds = genreIds,
    name = name,
    releaseYear =releaseYear,
    rating = rating,
)

fun ApiResponse<MovieRemote>.toLocal(query: String) = SearchMoviesLocal(
    date = System.currentTimeMillis(),
    query = query.generateHash(),
    page = currentPage,
    results = items.map { it.toLocal() },
    totalPages = totalPages,
    totalResults = totalItems
)

fun ApiResponse<MovieRemote>.toLocal() = UpComingSectionLocal(
    date = System.currentTimeMillis(),
    page = currentPage,
    results = items.map { it.toUpComingLocal() },
    totalPages = totalPages,
    totalResults = totalItems
)

fun UpComingMovieDtoLocal.toEntity() = UpComingMovie(
    id = id,
    imageUrl = imageUrl,
    genreIds = genreIds,
)

fun MovieRemote.toUpComingLocal() = UpComingMovieDtoLocal(
    id = id.orZero(),
    imageUrl = posterPath.asImageUrlOrEmpty(),
    genreIds = genreIds.orEmpty()
)

fun MovieRemote.toLocal() = SearchMovieDtoLocal(
    genreIds = genreIds.orEmpty(),
    id = id.orZero(),
    posterPath = posterPath.orEmpty(),
    name = name.orEmpty(),
    releaseYear = releaseDate?.extractYear().orZero(),
    rating = voteAverage?.toInt().orZero(),
)

fun MovieRemote.toEntity() = Movie(
    id = id.orZero(),
    name = name.orEmpty(),
    posterUrl = posterPath.asImageUrlOrEmpty(),
    releaseYear = releaseDate?.extractYear().orZero(),
    rating = voteAverage?.toInt().orZero(),
    genreIds = genreIds.orEmpty()
)