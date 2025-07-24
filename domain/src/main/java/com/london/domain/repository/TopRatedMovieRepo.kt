package com.london.domain.repository

import com.london.domain.entity.toprated.TopRatedMovie

interface TopRatedMovieRepo {
    suspend fun getTopRatedMovies(
        pageNumber: Int,
        language: String,
        region: String
    ): List<TopRatedMovie>
}