package com.london.domain

class MovieSearchFailedException(message: String = "Failed to search for movies.") : Exception(message)
class TvShowSearchFailedException(message: String = "Failed to search for TV shows.") : Exception(message)
class ActorSearchFailedException(message: String = "Failed to search for actors.") : Exception(message)

class TvShowDetailsSearchFailedException(message: String = "Failed to get TV show details.") : Exception(message)

class ActorDetailsSearchFailedException(message: String = "Failed to get actor details.") : Exception(message)
class GetCastByIdFailedException(message: String = "Failed to get cast") : Exception(message)
class GetImagesByIdFailedException(message: String = "Failed to get cast") : Exception(message)

class GetSimilarMoviesFailedException(message: String = "Failed to get similar movies") : Exception(message)
class GetMovieByIdFailedException(message: String = "Failed to get movie") : Exception(message)
class GetMovieImagesFailedException(message: String = "Failed to get movie images") : Exception(message)
class GetMovieCastFailedException(message: String = "Failed to get movie cast") : Exception(message)
class GetMovieDetailsFailedException(message: String = "Failed to get movie details") : Exception(message)

class GetReviewsFailedException(message: String = "Failed to get reviews") : Exception(message)

sealed class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class NoInternetException(message: String = "No internet connection.", cause: Throwable? = null) : NetworkException(message, cause)
}