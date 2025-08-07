package com.london.domain.repository

interface RatingRepository {
    suspend fun addMovieRatingById(
        id: Int,
        rating: Int,
    ) : Boolean

    suspend fun addTvShowById(
        id: Int,
        rating: Int
    ): Boolean

    suspend fun addTvEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Int
    ): Boolean
}