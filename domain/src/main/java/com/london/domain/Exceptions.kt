package com.london.domain

class MovieSearchFailedException(message: String = "Failed to search for movies.") : Exception(message)
class TvShowSearchFailedException(message: String = "Failed to search for TV shows.") : Exception(message)
class ActorSearchFailedException(message: String = "Failed to search for actors.") : Exception(message)

class TvShowDetailsSearchFailedException(message: String = "Failed to get TV show details.") : Exception(message)

class ActorDetailsSearchFailedException(message: String = "Failed to get actor details.") : Exception(message)
class GetCastByIdFailedException(message: String = "Failed to get cast") : Exception(message)
class GetImagesByIdFailedException(message: String = "Failed to get cast") : Exception(message)