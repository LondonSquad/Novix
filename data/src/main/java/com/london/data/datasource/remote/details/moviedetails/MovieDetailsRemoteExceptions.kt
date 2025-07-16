package com.london.data.datasource.remote.details.moviedetails

class GetMovieDetailsException(cause: Throwable) : Exception("Failed to fetch movie details", cause)
class GetSimilarMoviesException(cause: Throwable) : Exception("Failed to fetch similar movies", cause)
class GetMovieCastException(cause: Throwable) : Exception("Failed to fetch movie actorRemote", cause)
class GetMovieImagesException(cause: Throwable) : Exception("Failed to fetch movie images", cause)
