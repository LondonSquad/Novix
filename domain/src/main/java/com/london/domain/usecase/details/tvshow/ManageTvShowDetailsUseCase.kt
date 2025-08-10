package com.london.domain.usecase.details.tvshow

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.repository.PopularRepository
import com.london.domain.repository.SearchRepository
import com.london.domain.repository.TrendingRepository
import com.london.domain.repository.TvShowRepository
import com.london.domain.repository.discover.DiscoverRepository
import javax.inject.Inject

class ManageTvShowDetailsUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository,
    private val popularRepository: PopularRepository,
    private val trendingRepository: TrendingRepository,
    private val searchRepository: SearchRepository,
    private val discoverRepository: DiscoverRepository,
) {
    suspend fun getTvShowDetails(tvShowId: Int) = tvShowRepository.getTvShowDetailsById(tvShowId)

    suspend fun getPopularTvShows(limit: Int = LIMIT) =
        popularRepository.getPopularTvShows().take(limit)

    suspend fun getTrendingTvShows(page: Int, genreId: Int? = null): PagedFetchResponse<Trending> {
        val tvShows = trendingRepository.getTrendingTvShows(page = page)
        
        return if (genreId != null && genreId != -1) {
            val filteredItems = tvShows.items.filter { it.genreIds.contains(genreId) }
            tvShows.copy(items = filteredItems)
        } else {
            tvShows
        }
    }

    suspend fun getTvShowList(name: String, pageNumber: Int) =
        searchRepository.searchForTvShows(
            name = name,
            pageNumber = pageNumber
        )

    suspend fun getTvShowsByCategory(
        categoryId: Int, pageNumber: Int
    ) = discoverRepository.getTvShowsByCategory(
        categoryId = categoryId,
        pageNumber = pageNumber
    )

    suspend fun getTvShowVideoProvider(tvShowId: Int) =
        tvShowRepository.getTvShowVideos(tvShowId)

    companion object {
        const val LIMIT = 5
    }
}