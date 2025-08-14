package com.london.domain.usecase.toprated

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetTopRatedTvShowUseCase @Inject constructor(
    private val repository: TvShowRepository
) {
    suspend fun getAll(
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

    suspend fun getMostRecent(limit: Int = LIMIT) =
        repository.getFirstPageTopRatedTvShows().take(limit)

    companion object {
        const val LIMIT = 10
    }

}