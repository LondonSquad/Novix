package com.london.domain.usecase.details.movie

import com.london.domain.repository.MovieDetailsRepository
import javax.inject.Inject

class ManageMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieDetailsRepository
) {
    suspend fun getMovieDetails(movieId: Int) = movieRepository.getMovieById(movieId)

    suspend fun getFirstTenMovieImagesUseCase(movieId: Int) =
        movieRepository.getMovieImagesById(movieId).take(IMAGE_LIMIT)

    suspend fun getMovieCast(movieId: Int) = movieRepository.getMovieCastById(movieId)

    suspend fun getSimilarMovies(movieId: Int) = movieRepository.getSimilarMoviesById(movieId)

    suspend fun getMovieVideo(movieId: Int) = movieRepository.getMovieVideos(movieId)

    companion object {
        private const val IMAGE_LIMIT = 10
    }
}