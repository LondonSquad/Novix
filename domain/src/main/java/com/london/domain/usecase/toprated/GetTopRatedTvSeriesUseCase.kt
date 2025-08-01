package com.london.domain.usecase.toprated

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedTvSeries
import com.london.domain.repository.toprated.TopRatedTvSeriesRepository
import javax.inject.Inject

class GetTopRatedTvSeriesUseCase @Inject constructor(
    private val topRatedTvSeriesRepo: TopRatedTvSeriesRepository
) {
    suspend operator fun invoke(
        pageNumber: Int,
        genreId: Int? = null
    ) : PagedFetchResponse<TopRatedTvSeries> {
        val response = topRatedTvSeriesRepo.getTopRatedTvSeries(pageNumber)

        val filteredItems = response.items.filter { movie ->
            genreId == null || movie.genreIds.contains(genreId)
        }

        return response.copy(
            items = filteredItems,
            totalPages = filteredItems.size
        )
    }
}