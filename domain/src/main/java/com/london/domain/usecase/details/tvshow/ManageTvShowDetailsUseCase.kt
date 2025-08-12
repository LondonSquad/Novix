package com.london.domain.usecase.details.tvshow

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.repository.SearchRepository
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class ManageTvShowDetailsUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository,
    private val searchRepository: SearchRepository,
) {
    suspend fun getTvShowDetails(tvShowId: Int) = tvShowRepository.getTvShowDetailsById(tvShowId)

    suspend fun getPopularTvShows(limit: Int = LIMIT) =
        tvShowRepository.getPopularTvShows().take(limit)

    suspend fun getTrendingTvShows(page: Int, movieGenreId: Int?): PagedFetchResponse<Trending> {
        val trendingTvShow = tvShowRepository.getTrendingTvShows(page)

        val genreId = if (movieGenreId == -1) null else movieGenreId

        val filteredItems = if (genreId != null)
            trendingTvShow.items.filter { it.genreIds.contains(genreId) }
        else
            trendingTvShow.items

        return PagedFetchResponse(
            currentPage = trendingTvShow.currentPage,
            items = filteredItems,
            totalPages = trendingTvShow.totalPages,
            totalItems = trendingTvShow.totalItems,
        )
    }

    suspend fun getTvShowList(name: String, pageNumber: Int) =
        searchRepository.searchForTvShows(
            name = name,
            pageNumber = pageNumber
        )

    suspend fun getTvShowsByCategory(
        categoryId: Int, pageNumber: Int
    ) = tvShowRepository.getTvShowsByCategory(
        categoryId = categoryId,
        pageNumber = pageNumber
    )

    suspend fun getTvShowVideoProvider(tvShowId: Int) =
        tvShowRepository.getTvShowVideos(tvShowId)

    companion object {
        const val LIMIT = 5
    }
}