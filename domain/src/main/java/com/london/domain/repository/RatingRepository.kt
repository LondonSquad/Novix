package com.london.domain.repository

import com.london.domain.entity.RatedMedia

interface RatingRepository {
    suspend fun addMovieRatingById(id: Int, rating: Int): Boolean

    suspend fun addTvShowById(id: Int, rating: Int): Boolean

    suspend fun addTvEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Int
    ): Boolean

    suspend fun getAllRatedMedia(): List<RatedMedia>

    suspend fun getAllRatedMovies(): List<RatedMedia>

    suspend fun getAllRatedTvShows(): List<RatedMedia>

    suspend fun deleteMovieRating(movieId: Int): Boolean

    suspend fun deleteTvShowRating(tvShowId: Int): Boolean
}