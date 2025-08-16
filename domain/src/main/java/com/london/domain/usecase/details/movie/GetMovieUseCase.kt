package com.london.domain.usecase.details.movie

import com.london.domain.entity.Actor
import com.london.domain.entity.Movie
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.Trending
import com.london.domain.entity.UpComingMovie
import com.london.domain.entity.genre.MovieGenre
import com.london.domain.entity.popular.PopularMedia
import com.london.domain.entity.review.ReviewEntity
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

    suspend fun getMovieReviews(movieId: Int, pageNumber: Int): PagedFetchResponse<ReviewEntity> =
        movieRepository.getMovieReviews(movieId, pageNumber)

    suspend fun getMovieCast(movieId: Int): List<Actor> = actorRepository.getMovieActors(movieId)

    suspend fun getSimilarMovies(movieId: Int): List<Movie> =
        movieRepository.getSimilarMoviesById(movieId)

    suspend fun getMovieVideo(movieId: Int): List<String> = movieRepository.getMovieVideos(movieId)

    suspend fun getTrendingMovies(
        page: Int,
        movieGenre: MovieGenre = MovieGenre.ALL
    ): PagedFetchResponse<Trending> {
        val trendingMovies = movieRepository.getTrendingMovies(page)

        val filteredItems = trendingMovies.items.filter { movie ->
            movieGenre == MovieGenre.ALL || movie.genres.contains(movieGenre)
        }

        return trendingMovies.copy(items = filteredItems, totalPages = filteredItems.size)
    }

    suspend fun getPopularMovies(limit: Int = POPULAR_LIMIT): List<PopularMedia> =
        movieRepository.getPopularMovies().take(limit)

    suspend fun getMoviesByGenre(
        genre: MovieGenre, pageNumber: Int
    ): PagedFetchResponse<Movie> = movieRepository.getMoviesByGenre(
        genre = genre,
        pageNumber = pageNumber
    )

    suspend fun getUpcomingMoviesByGenre(
        genre: MovieGenre, pageNumber: Int
    ): PagedFetchResponse<UpComingMovie> = movieRepository.getUpcomingMoviesByGenre(
        genre = genre,
        pageNumber = pageNumber
    )

    suspend fun getAllTopRatedMovies(
        pageNumber: Int,
        genre: MovieGenre = MovieGenre.ALL
    ): PagedFetchResponse<TopRatedMedia> {
        val response = movieRepository.getTopRatedMovies(pageNumber)

        val filteredItems = response.items.filter { movie ->
            genre == MovieGenre.ALL || movie.genres.contains(genre)
        }

        return response.copy(
            items = filteredItems,
            totalPages = filteredItems.size
        )
    }

    suspend fun getMostRecentMovies(limit: Int = TOP_RATED_LIMIT): List<TopRatedMedia> =
        movieRepository.getFirstPageTopRatedMovies().take(limit)

    companion object {
        private const val IMAGE_LIMIT = 10
        private const val POPULAR_LIMIT = 5
        private const val TOP_RATED_LIMIT = 10
    }
}
