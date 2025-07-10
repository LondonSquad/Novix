package com.london.domain

class MovieSearchFailedException(message: String = "Failed to search for movies.") : Exception(message)
class TvShowSearchFailedException(message: String = "Failed to search for TV shows.") : Exception(message)
class ActorSearchFailedException(message: String = "Failed to search for actors.") : Exception(message)