package com.london.domain

open class DomainException(message: String) : Exception(message)

class MovieSearchFailedException(message: String = "Failed to search for movies.") : DomainException(message)
class TvShowSearchFailedException(message: String = "Failed to search for TV shows.") : DomainException(message)
class ActorSearchFailedException(message: String = "Failed to search for actors.") : DomainException(message)