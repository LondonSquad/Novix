package com.london.domain.repository.toprated

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedMovie
import com.london.domain.entity.toprated.TopRatedTvSeries


interface TopRatedRepository {

    suspend fun getTopRatedMovies(
        pageNumber: Int,
    ): PagedFetchResponse<TopRatedMovie>

    suspend fun getTopRatedTvSeries(
        pageNumber: Int,
    ): PagedFetchResponse<TopRatedTvSeries>
}