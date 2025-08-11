package com.london.domain.usecase.toprated

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetTopRatedTvSeriesUseCase @Inject constructor(
    private val repository: TvShowRepository
) {
    suspend operator fun invoke(
        pageNumber: Int,
        genreId: Int? = null
    ) : PagedFetchResponse<TopRatedMedia> {
        val response = repository.getTopRatedTvShows(pageNumber)

        val filteredItems = response.items.filter { movie ->
            genreId == null || movie.genreIds.contains(genreId)
        }

        return response.copy(
            items = filteredItems,
            totalPages = filteredItems.size
        )
    }
}