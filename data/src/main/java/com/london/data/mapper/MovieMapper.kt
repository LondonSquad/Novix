package com.london.data.mapper

import com.london.data.datasource.local.model.SearchMovieDtoLocal
import com.london.data.datasource.remote.search.SearchMoviesResponseDto
import com.london.domain.entity.Movie

fun SearchMoviesResponseDto.toMovieEntity(): Movie {
    return this.posterPath?.let {
        Movie(
            id = this.id,
            posterPicture = it
        )
    } ?: Movie(id = this.id, posterPicture = "")
}

fun SearchMovieDtoLocal.toMovieEntity(): Movie {
    return this.posterPath?.let {
        Movie(
            id = this.id,
            posterPicture = it
        )
    } ?: Movie(id = this.id, posterPicture = "")
}
