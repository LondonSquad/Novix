package com.london.domain.usecase

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedTvSeries
import com.london.domain.repository.TopRatedTvSeriesRepository
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Single

@Single
class GetTopRatedTvSeriesUseCase(
    @Provided
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