package com.london.data.repository.myrating

import android.content.ContentValues.TAG
import com.london.data.local.preference.AuthPreferences
import com.london.data.mapper.list.toRatedMovie
import com.london.data.remote.source.myrating.MyRatingRemoteDataSource
import com.london.data.utils.fetchAndSync
import com.london.domain.entity.myrating.RatedMovie
import com.london.domain.repository.myrating.MyRatingRepository
import timber.log.Timber
import javax.inject.Inject

class MyRatingRepositoryImpl @Inject constructor(
    private val myRatingRemoteDataSource: MyRatingRemoteDataSource,
    private val authPreferences: AuthPreferences,
) : MyRatingRepository {
//    override suspend fun getAllRatedMovies(): List<RatedMovie> = fetchAndSync(
//        networkBlock = {
//            Timber.i("toprated,$myRatingRemoteDataSource.getAllRatedMovies(\n" +
//                    "                accountId = accountId,\n" +
//                    "                sessionId = sessionId.orEmpty(),\n" +
//                    "            ).getOrThrow().items.map { it.toRatedMovie() }")
//            val accountId = 22144309 //change it
//            val sessionId = authPreferences.getSessionId()
//            myRatingRemoteDataSource.getAllRatedMovies(
//                accountId = accountId,
//                sessionId = sessionId.orEmpty(),
//            ).getOrThrow().items.map { it.toRatedMovie() }
//        }
//    )
override suspend fun getAllRatedMovies(): List<RatedMovie> = fetchAndSync(
    networkBlock = {
        Timber.tag(TAG).d("Starting getAllRatedMovies request")

        val accountId = 22144309 // change it
        val sessionId = authPreferences.getSessionId()

        Timber.tag(TAG).d("Account ID: $accountId")
        Timber.tag(TAG).d("Session ID: ${if (sessionId.isNullOrEmpty()) "null/empty" else "available"}")

        try {
            val response = myRatingRemoteDataSource.getAllRatedMovies(
                accountId = accountId,
                sessionId = sessionId.orEmpty(),
            )

            Timber.tag(TAG).d("Remote API call successful")

            val items = response.getOrThrow().items
            Timber.tag(TAG).d("Retrieved ${items.size} rated movies from API")

            val ratedMovies = items.map { it.toRatedMovie() }
            Timber.tag(TAG).d("Successfully mapped ${ratedMovies.size} movies to domain entities")

            ratedMovies

        } catch (e: Exception) {
            Timber.tag(TAG).e(e, "Error fetching rated movies: ${e.message}")
            throw e
        }
    }
)
}