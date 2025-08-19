package com.london.domain.usecase.details.tvshow

import com.london.domain.entity.genre.TvShowGenre
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.review.Review
import com.london.domain.entity.shared.PagedFetchResponse
import com.london.domain.entity.shared.Trending
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.entity.tvshow.TvShow
import com.london.domain.entity.tvshow.TvShowDetails
import com.london.domain.entity.tvshow.cast.TvShowCast
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.SearchRepository
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetTvShowUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository,
    private val searchRepository: SearchRepository,
    private val actorRepository: ActorRepository
) {
    suspend fun getTvShowDetails(tvShowId: Int): TvShowDetails =
        tvShowRepository.getTvShowDetailsById(tvShowId)

    suspend fun getPopularTvShows(limit: Int = POPULAR_LIMIT): List<PopularMedia> =
        tvShowRepository.getPopularTvShows().take(limit)

    suspend fun getTrendingTvShows(
        page: Int,
        tvShowGenre: TvShowGenre = TvShowGenre.ALL
    ): PagedFetchResponse<Trending> {
        val trendingMovies = tvShowRepository.getTrendingTvShows(page)

        val filteredItems = trendingMovies.items.filter {
            tvShowGenre == TvShowGenre.ALL || it.genres.contains(tvShowGenre)
        }

        return trendingMovies.copy(
            items = filteredItems,
            totalPages = filteredItems.size,
        )
    }

    suspend fun getTvShowList(name: String, pageNumber: Int): PagedFetchResponse<TvShow> =
        searchRepository.searchForTvShows(
            name = name,
            pageNumber = pageNumber
        )

    suspend fun getTvShowsByGenre(
        genre: TvShowGenre, pageNumber: Int
    ): PagedFetchResponse<TvShow> = tvShowRepository.getTvShowsByGenre(
        genre = genre,
        pageNumber = pageNumber
    )

    suspend fun getTvSeasonTrailer(tvShowId: Int, seasonNumber: Int): List<String> =
        tvShowRepository.getTvSeasonTrailer(tvShowId = tvShowId, seasonNumber = seasonNumber)

    suspend fun getImagesTvShowById(tvShowId: Int, limit: Int = IMAGE_LIMIT): List<String> {
        val images = tvShowRepository.getImagesTvShowById(tvShowId)
        return when {
            images.backdropsUrl.isNotEmpty() -> images.backdropsUrl
            images.postersUrl.isNotEmpty() -> images.postersUrl
            images.logosUrl.isNotEmpty() -> images.logosUrl
            else -> emptyList()
        }.take(limit)
    }

    suspend fun getTvShowReviews(tvShowId: Int, pageNumber: Int): PagedFetchResponse<Review> =
        tvShowRepository.getTvShowReviews(tvShowId = tvShowId, pageNumber = pageNumber)

    suspend fun getTvShowCastById(id: Int): TvShowCast =
        actorRepository.getTvShowActors(id)

    suspend fun getAllTopRatedTvShows(
        pageNumber: Int,
        genre: TvShowGenre = TvShowGenre.ALL
    ): PagedFetchResponse<TopRatedMedia> {
        val response = tvShowRepository.getTopRatedTvShows(pageNumber)

        val filteredItems = response.items.filter { movie ->
            genre == TvShowGenre.ALL || movie.genres.contains(genre)
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
