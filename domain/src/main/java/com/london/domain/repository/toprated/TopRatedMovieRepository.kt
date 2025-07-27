package com.london.domain.repository.toprated

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedMovie


interface TopRatedMovieRepository {
    suspend fun getTopRatedMovies(
        pageNumber: Int,
    ): PagedFetchResponse<TopRatedMovie>
}