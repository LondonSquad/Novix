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

    suspend fun getPopularTvShows(limit: Int = POPULAR_LIMIT) =
        popularRepository.getPopularTvShows().take(limit)

    suspend fun getTrendingTvShows(page: Int): PagedFetchResponse<Trending> =
        trendingRepository.getTrendingTvShows(page = page)

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

    suspend fun getImagesTvShowById(tvShowId: Int, limit: Int = IMAGE_LIMIT): List<String> {
        val images = tvShowRepository.getImagesTvShowById(tvShowId)
        return when {
            images.backdropsUrl.isNotEmpty() -> images.backdropsUrl
            images.postersUrl.isNotEmpty() -> images.postersUrl
            images.logosUrl.isNotEmpty() -> images.logosUrl
            else -> emptyList()
        }.take(limit)
    }

    companion object {
        const val IMAGE_LIMIT = 10
        const val POPULAR_LIMIT = 5
    }
}