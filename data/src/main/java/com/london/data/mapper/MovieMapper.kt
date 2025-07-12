package com.london.data.mapper

import com.london.data.datasource.local.model.SearchMovieDtoLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.remote.search.SearchMoviesResponse
import com.london.data.datasource.remote.search.SearchMoviesResponseDto
import com.london.data.datasource.util.generateHash
import com.london.domain.entity.Movie

fun SearchMovieDtoLocal.toMovieEntity(): Movie {
    return this.posterPath?.let {
        Movie(
            id = this.id,
            posterPicture = it,
            name = this.title,
        )
    } ?: Movie(id = this.id, posterPicture = "", name = this.title)
}

fun SearchMoviesResponse.toLocal(query: String): SearchMoviesLocal {
    return SearchMoviesLocal(
        date = System.currentTimeMillis(),
        query = query.generateHash(),
        page = this.page,
        results = this.results.map { it.toLocalDto() },
        totalPages = this.totalPages,
        totalResults = this.totalResults
    )
}


fun SearchMoviesResponseDto.toLocalDto(): SearchMovieDtoLocal {
    return SearchMovieDtoLocal(
        adult = this.adult,
        backdropPath = this.backdropPath,
        genreIds = this.genreIds,
        id = this.id,
        originalLanguage = this.originalLanguage,
        originalTitle = this.originalTitle,
        overview = this.overview,
        popularity = this.popularity,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate,
        title = this.title,
        video = this.video,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount
    )
}