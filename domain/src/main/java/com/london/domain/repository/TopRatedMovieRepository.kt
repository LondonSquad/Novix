package com.london.domain.repository

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedMovie


interface TopRatedMovieRepository {
    suspend fun getTopRatedMovies(
        pageNumber: Int,
    ): PagedFetchResponse<TopRatedMovie>
}