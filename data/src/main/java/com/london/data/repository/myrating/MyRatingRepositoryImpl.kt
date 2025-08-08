package com.london.data.repository.myrating

import com.london.data.local.preference.AuthPreferences
import com.london.data.mapper.myrating.toEntity
import com.london.data.remote.source.myrating.MyRatingRemoteDataSource
import com.london.data.utils.fetchAndSync
import com.london.domain.entity.RatedMedia
import com.london.domain.repository.myrating.MyRatingRepository
import javax.inject.Inject

class MyRatingRepositoryImpl @Inject constructor(
    private val myRatingRemoteDataSource: MyRatingRemoteDataSource,
    private val authPreferences: AuthPreferences,
) : MyRatingRepository {

    override suspend fun getAllRatedMedia(): List<RatedMedia> = fetchAndSync(
        networkBlock = {
            val accountId = authPreferences.getAccountId()
            val sessionId = authPreferences.getSessionId()
            
            val movies = myRatingRemoteDataSource.getAllRatedMovies(
                accountId = accountId,
                sessionId = sessionId.orEmpty(),
            ).getOrThrow().items.map { it.toEntity(isMovie = true) }
            
            val tvShows = myRatingRemoteDataSource.getAllRatedTvShows(
                accountId = accountId,
                sessionId = sessionId.orEmpty(),
            ).getOrThrow().items.map { it.toEntity(isMovie = false) }
            
            movies + tvShows
        }
    )
}
