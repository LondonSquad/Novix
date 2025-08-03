package com.london.domain.repository

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.UpComingMovie

interface UpComingRepository {
    suspend fun getUpComingMoviesByCategory(
        categoryId: Int?, pageNumber: Int
    ): PagedFetchResponse<UpComingMovie>
}