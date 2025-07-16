package com.london.domain.usecase

import com.london.domain.entity.moviedatails.MovieDetails


class GetMovieDetailsUseCase(
    private val getMovieById: GetMovieById,
    private val getMovieImagesUseCase: GetMovieImagesUseCase,
    private val getMovieCastUseCase: GetMovieCastUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase
) {
    suspend operator fun invoke(movieId: Int): MovieDetails {
        val getMovieById = getMovieById(movieId)
        val movieImages = getMovieImagesUseCase(movieId)
        val movieCast = getMovieCastUseCase(movieId)
        val similarMovies = getSimilarMoviesUseCase(movieId)
        return MovieDetails(
            movieId = getMovieById.movieId,
            movieImage = movieImages,
            movieName = getMovieById.movieName,
            movieRating =getMovieById.movieRating,
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