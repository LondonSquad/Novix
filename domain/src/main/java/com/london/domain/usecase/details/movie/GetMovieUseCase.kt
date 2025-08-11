package com.london.domain.usecase.details.movie

import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.entity.toprated.TopRatedMedia
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val actorRepository: ActorRepository,
) {
    suspend fun getMovieDetails(movieId: Int) = movieRepository.getMovieById(movieId)

    suspend fun getMovieImagesUseCase(movieId: Int, limit: Int = IMAGE_LIMIT): List<String> {
        val images = movieRepository.getMovieImagesById(movieId)
        return when {
            images.backdrops.isNotEmpty() -> images.backdrops
            images.posters.isNotEmpty() -> images.posters
            images.logos.isNotEmpty() -> images.logos
            else -> emptyList()
        }.take(limit)
    }

    suspend fun getMovieReviews(movieId: Int, pageNumber: Int) =
        movieRepository.getMovieReviews(movieId, pageNumber)

    suspend fun getMovieCast(movieId: Int) = actorRepository.getMovieCastById(movieId)

    suspend fun getSimilarMovies(movieId: Int) = movieRepository.getSimilarMoviesById(movieId)

    suspend fun getMovieVideo(movieId: Int) = movieRepository.getMovieVideos(movieId)

    suspend fun getTrendingMovies(page: Int): PagedFetchResponse<Trending> =
        movieRepository.getTrendingMovies(page)

    suspend fun getPopularMovies(limit: Int = POPULAR_LIMIT) =
        movieRepository.getPopularMovies().take(limit)

    suspend fun getMoviesByCategory(
        categoryId: Int, pageNumber: Int
    ) = movieRepository.getMoviesByCategory(
        categoryId = categoryId,
        pageNumber = pageNumber
    )

    suspend fun getUpcomingMoviesByCategory(
        categoryId: Int?, pageNumber: Int
    ) = movieRepository.getUpcomingMoviesByCategory(
        categoryId = categoryId,
        pageNumber = pageNumber
    )

    suspend fun getTopRatedMovies(
        pageNumber: Int,
        genreId: Int? = null
    ): PagedFetchResponse<TopRatedMedia> {
        val response = movieRepository.getTopRatedMovies(pageNumber)

        val filteredItems = response.items.filter { movie ->
            genreId == null || movie.genreIds.contains(genreId)
        }

        return response.copy(
            items = filteredItems,
            totalPages = filteredItems.size
        )
    }
    companion object {
        private const val IMAGE_LIMIT = 10
        private const val POPULAR_LIMIT = 5
    }
}