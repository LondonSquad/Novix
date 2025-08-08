package com.london.data.repository.myrating

import com.london.data.local.preference.AuthPreferences
import com.london.data.mapper.list.toRatedMovie
import com.london.data.mapper.list.toRatedTvShow
import com.london.data.remote.source.myrating.MyRatingRemoteDataSource
import com.london.data.utils.fetchAndSync
import com.london.domain.entity.myrating.RatedMovie
import com.london.domain.entity.myrating.RatedTvShow
import com.london.domain.repository.myrating.MyRatingRepository
import javax.inject.Inject

class MyRatingRepositoryImpl @Inject constructor(
    private val myRatingRemoteDataSource: MyRatingRemoteDataSource,
    private val authPreferences: AuthPreferences,
) : MyRatingRepository {

    override suspend fun getAllRatedMovies(): List<RatedMovie> = fetchAndSync(
        networkBlock = {
            val accountId = 22144309 //change it
            val sessionId = authPreferences.getSessionId()
            myRatingRemoteDataSource.getAllRatedMovies(
                accountId = accountId,
                sessionId = sessionId.orEmpty(),
            ).getOrThrow().items.map { it.toRatedMovie() }
        }
    )

    override suspend fun getAllRatedMTvShows(): List<RatedTvShow> = fetchAndSync(
        networkBlock = {
            val accountId = 22144309 //change it
            val sessionId = authPreferences.getSessionId()
            myRatingRemoteDataSource.getAllRatedTvShow(
                accountId = accountId,
                sessionId = sessionId.orEmpty(),
            ).getOrThrow().items.map { it.toRatedTvShow() }
        }
    )
}