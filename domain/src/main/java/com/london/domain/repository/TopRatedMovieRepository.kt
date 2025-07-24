package com.london.domain.repository

import com.london.domain.entity.toprated.TopRatedMovie

interface TopRatedMovieRepository {
    suspend fun getTopRatedMovies(
        pageNumber: Int,
        language: String,
        region: String
    ): List<TopRatedMovie>
}