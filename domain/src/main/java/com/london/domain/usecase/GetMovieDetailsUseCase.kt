package com.london.domain.usecase

import com.london.domain.entity.moviedatails.MovieDetails
import org.koin.core.annotation.Single

@Single
class GetMovieDetailsUseCase(
    private val getMovieById: GetMovieById,
    private val getMovieImagesUseCase: GetMovieImagesUseCase,
    private val getMovieCastUseCase: GetMovieCastUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase
) {
    suspend operator fun invoke(movieId: Int): MovieDetails {
        val getMovieById = getMovieById.invoke(movieId)
        getMovieImagesUseCase.invoke(movieId)
        val movieCast = getMovieCastUseCase.invoke(movieId)
        val similarMovies = getSimilarMoviesUseCase.invoke(movieId)
        return MovieDetails(
            movieId = getMovieById.movieId,
            movieImage = getMovieById.movieImage,
            movieName = getMovieById.movieName,
            movieRating = getMovieById.movieRating,
            movieDuration = getMovieById.movieDuration,
            releaseDate = getMovieById.releaseDate,
            movieOverview = getMovieById.movieOverview,
            genres = getMovieById.genres,
            actors = movieCast,
            movieHaveTrailer = getMovieById.movieHaveTrailer,
            similarMovies = similarMovies
        )
    }
}