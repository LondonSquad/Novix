package com.london.data.mapper

import com.london.data.datasource.local.model.SearchMovieDtoLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.remote.ApiResponse
import com.london.data.datasource.remote.search.model.SearchMovieRemote
import com.london.data.datasource.util.generateHash
import com.london.domain.KoverIgnore
import com.london.domain.entity.Movie

fun SearchMovieDtoLocal.toMovieEntity(): Movie {
    return Movie(
        id = this.id,
        posterPicture = "https://image.tmdb.org/t/p/w500${this.posterPath}",
        name = this.title,
        releaseYear = if (this.releaseDate.isNotEmpty())
            this.releaseDate.split("-")[0].toInt() else 0,
        rating = this.voteAverage.toInt(),
        genreIds = this.genreIds,
    )
}

fun ApiResponse<SearchMovieRemote>.toLocal(query: String): SearchMoviesLocal {
    return SearchMoviesLocal(
        date = System.currentTimeMillis(),
        query = query.generateHash(),
        page = this.currentPage,
        results = this.items.map { it.toMovieLocalDto() },
        totalPages = this.totalPages,
        totalResults = this.totalItems
    )
}

@KoverIgnore
fun SearchMovieRemote.toMovieLocalDto(): SearchMovieDtoLocal {
    return SearchMovieDtoLocal(
        adult = this.adult,
        backdropPath = this.backdropPath,
        genreIds = this.genreIds,
        id = this.id,
        originalLanguage = this.originalLanguage ?: "",
        originalTitle = this.originalTitle ?: "",
        overview = this.overview ?: "",
        popularity = this.popularity,
        posterPath = this.posterPath,
        releaseDate = this.releaseDate ?: "",
        title = this.title ?: "",
        video = this.video,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount
    )
}