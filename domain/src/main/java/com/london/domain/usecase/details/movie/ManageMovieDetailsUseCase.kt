package com.london.domain.usecase.details.movie

import com.london.domain.repository.ActorRepository
import com.london.domain.repository.MovieRepository
import javax.inject.Inject

class ManageMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val actorRepository: ActorRepository,
) {
    suspend fun getMovieDetails(movieId: Int) = movieRepository.getMovieById(movieId)

    suspend fun getMovieImagesUseCase(movieId: Int, limit: Int = LIMIT) : List<String> {
           val images = movieRepository.getMovieImagesById(movieId)
            return when {
                images.backdrops.isNotEmpty() -> images.backdrops
                images.posters.isNotEmpty() -> images.posters
                images.logos.isNotEmpty() -> images.logos
                else -> emptyList()
            }.take(limit)
        }

    suspend fun getMovieCast(movieId: Int) = actorRepository.getMovieActors(movieId)

    suspend fun getSimilarMovies(movieId: Int) = movieRepository.getSimilarMoviesById(movieId)

    suspend fun getMovieVideo(movieId: Int) = movieRepository.getMovieVideos(movieId)

    companion object {
        private const val LIMIT = 10
    }
}