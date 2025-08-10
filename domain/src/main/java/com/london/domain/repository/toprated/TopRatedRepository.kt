package com.london.domain.repository.toprated

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedMedia


interface TopRatedRepository {

    suspend fun getTopRatedMovies(
        pageNumber: Int,
    ): PagedFetchResponse<TopRatedMedia>

    suspend fun getTopRatedTvSeries(
        pageNumber: Int,
    ): PagedFetchResponse<TopRatedMedia>
}