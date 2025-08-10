package com.london.domain.usecase.details.movie

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.repository.MovieDetailsRepository
import com.london.domain.repository.PopularRepository
import com.london.domain.repository.TrendingRepository
import com.london.domain.repository.discover.DiscoverRepository
import javax.inject.Inject

class ManageMovieUseCase @Inject constructor(
    private val movieRepository: MovieDetailsRepository,
    private val trendingRepository: TrendingRepository,
    private val popularRepository: PopularRepository,
    private val discoverRepository: DiscoverRepository
) {
    suspend fun getMovieDetails(movieId: Int) = movieRepository.getMovieById(movieId)

    suspend fun getMovieImagesUseCase(movieId: Int, limit: Int = LIMIT): List<String> {
        val images = movieRepository.getMovieImagesById(movieId)
        return when {
            images.backdrops.isNotEmpty() -> images.backdrops
            images.posters.isNotEmpty() -> images.posters
            images.logos.isNotEmpty() -> images.logos
            else -> emptyList()
        }.take(limit)
    }

    suspend fun getMovieCast(movieId: Int) = movieRepository.getMovieCastById(movieId)

    suspend fun getSimilarMovies(movieId: Int) = movieRepository.getSimilarMoviesById(movieId)

    suspend fun getMovieVideo(movieId: Int) = movieRepository.getMovieVideos(movieId)

    suspend fun getTrendingMovies(page: Int): PagedFetchResponse<Trending> =
        trendingRepository.getTrendingMovies(page)

    suspend fun getPopularMovies(limit: Int = POPULAR_LIMIT) =
        popularRepository.getPopularMovies().take(limit)

    suspend fun getMoviesByCategory(
        categoryId: Int, pageNumber: Int
    ) = discoverRepository.getMoviesByCategory(
        categoryId = categoryId,
        pageNumber = pageNumber
    )

    companion object {
        private const val LIMIT = 10
        private const val POPULAR_LIMIT = 5
    }
}