package com.london.data.repository.myrating

import com.london.data.local.preference.AuthPreferences
import com.london.data.mapper.myrating.toEntity
import com.london.data.remote.source.myrating.RatingRemoteDataSource
import com.london.data.utils.fetchAndSync
import com.london.domain.entity.RatedMedia
import com.london.domain.entity.recent.MediaType
import com.london.domain.repository.RatingRepository
import javax.inject.Inject

class RatingRepositoryImpl @Inject constructor(
    private val ratingRemoteDataSource: RatingRemoteDataSource,
    private val authPreferences: AuthPreferences
) : RatingRepository {

    override suspend fun addMovieRatingById(
        id: Int,
        rating: Int,
    ): Boolean = ratingRemoteDataSource.addMovieRating(
        movieId = id,
        rating = rating.toDouble(),
        userSessionId = authPreferences.getSessionId(),
        guestSessionId = authPreferences.getGuestSessionId()
    ).isSuccess

    override suspend fun addTvShowById(id: Int, rating: Int): Boolean =
        ratingRemoteDataSource.addTvShowRating(
            tvShowId = id,
            rating = rating.toDouble(),
            userSessionId = authPreferences.getSessionId(),
            guestSessionId = authPreferences.getGuestSessionId()
        ).isSuccess

    override suspend fun addTvEpisode(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int,
        rating: Int
    ): Boolean = ratingRemoteDataSource.addTvEpisode(
        tvShowId = tvShowId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
        userSessionId = authPreferences.getSessionId(),
        guestSessionId = authPreferences.getGuestSessionId(),
        rating = rating.toDouble()
    ).isSuccess

    override suspend fun getAllRatedMedia(): List<RatedMedia> = fetchAndSync(
        networkBlock = {
            val accountId = authPreferences.getAccountId()
            val sessionId = authPreferences.getSessionId()

            val movies = ratingRemoteDataSource.getAllRatedMovies(
                accountId = accountId,
                sessionId = sessionId.orEmpty(),
            ).getOrThrow().items.map { it.toEntity(mediaType = MediaType.Movie) }

            val tvShows = ratingRemoteDataSource.getAllRatedTvShows(
                accountId = accountId,
                sessionId = sessionId.orEmpty(),
            ).getOrThrow().items.map { it.toEntity(mediaType = MediaType.TvShow) }

            movies + tvShows
        }
    )

    override suspend fun getAllRatedMovies(): List<RatedMedia> =
        ratingRemoteDataSource.getAllRatedMovies(
            accountId = authPreferences.getAccountId(),
            sessionId = authPreferences.getSessionId().orEmpty()
        ).getOrThrow().items.map { it.toEntity(mediaType = MediaType.Movie) }

    override suspend fun getAllRatedTvShows(): List<RatedMedia> =
        ratingRemoteDataSource.getAllRatedTvShows(
            accountId = authPreferences.getAccountId(),
            sessionId = authPreferences.getSessionId().orEmpty()
        ).getOrThrow().items.map { it.toEntity(mediaType = MediaType.TvShow) }

    override suspend fun deleteMovieRating(movieId: Int): Boolean =
        ratingRemoteDataSource.deleteMovieRating(
            movieId = movieId,
            sessionId = authPreferences.getSessionId()
        ).isSuccess

    override suspend fun deleteTvShowRating(tvShowId: Int): Boolean =
        ratingRemoteDataSource.deleteTvShowRating(
            tvShowId = tvShowId,
            sessionId = authPreferences.getSessionId()
        ).isSuccess
}
