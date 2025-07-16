package com.london.domain.usecase

import com.london.domain.entity.moviedatails.MovieDetails


class GetMovieDetailsUseCase(
    private val getMovieById: GetMovieById,
    private val getMovieImagesUseCase: GetMovieImagesUseCase,
    private val getMovieCastUseCase: GetMovieCastUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase
) {
    suspend operator fun invoke(movieId: Int): MovieDetails {
        val getMovieById = getMovieById.invoke(movieId)
        val movieImages = getMovieImagesUseCase.invoke(movieId)
        val movieCast = getMovieCastUseCase.invoke(movieId)
        val similarMovies = getSimilarMoviesUseCase.invoke(movieId)
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