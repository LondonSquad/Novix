package com.london.domain.usecase.details.tvshow

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.entity.TvShow
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.review.ReviewEntity
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.TvShowDetailsEntity
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.SearchRepository
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetTvShowUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository,
    private val searchRepository: SearchRepository,
    private val actorRepository: ActorRepository
) {
    suspend fun getTvShowDetails(tvShowId: Int): TvShowDetailsEntity =
        tvShowRepository.getTvShowDetailsById(tvShowId)

    suspend fun getPopularTvShows(limit: Int = POPULAR_LIMIT) : List<PopularMedia> =
        tvShowRepository.getPopularTvShows().take(limit)

    suspend fun getTrendingTvShows(page: Int): PagedFetchResponse<Trending> =
        tvShowRepository.getTrendingTvShows(page = page)

    suspend fun getTvShowList(name: String, pageNumber: Int) : PagedFetchResponse<TvShow> =
        searchRepository.searchForTvShows(
            name = name,
            pageNumber = pageNumber
        )

    suspend fun getTvShowsByCategory(
        categoryId: Int, pageNumber: Int
    ) : PagedFetchResponse<TvShow> = tvShowRepository.getTvShowsByCategory(
        categoryId = categoryId,
        pageNumber = pageNumber
    )

    suspend fun getTvShowVideo(tvShowId: Int) : List<String> =
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

    suspend fun getTvShowReviews(movieId: Int, pageNumber: Int) : PagedFetchResponse<ReviewEntity> =
        tvShowRepository.getTvShowReviews(movieId, pageNumber)

    suspend fun getTvShowCastById(tvShowId: Int): TvShowCastEntity =
        actorRepository.getCastTvShowById(tvShowId)

    suspend fun getAllTopRatedTvShows(
        pageNumber: Int,
        genreId: Int? = null
    ): PagedFetchResponse<TopRatedMedia> {
        val response = tvShowRepository.getTopRatedTvShows(pageNumber)

        val filteredItems = response.items.filter { movie ->
            genreId == null || movie.genreIds.contains(genreId)
        }

        return response.copy(
            items = filteredItems,
            totalPages = filteredItems.size
        )
    }

    suspend fun getMostRecentTvShows(limit: Int = TOP_RATED_LIMIT) =
        tvShowRepository.getFirstPageTopRatedTvShows().take(limit)

    companion object {
        const val IMAGE_LIMIT = 10
        const val POPULAR_LIMIT = 5
        const val TOP_RATED_LIMIT = 10
    }
}
