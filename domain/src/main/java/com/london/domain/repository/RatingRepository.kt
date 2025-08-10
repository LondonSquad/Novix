package com.london.domain.repository

import com.london.domain.entity.RatedMedia

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

    suspend fun getAllRatedMedia(): List<RatedMedia>
}