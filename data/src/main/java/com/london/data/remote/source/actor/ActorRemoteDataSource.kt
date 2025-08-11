package com.london.data.remote.source.actor

import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.actor.model.ActorDetailsResponse
import com.london.data.remote.model.details.actor.model.actorimage.ActorImageResponse
import com.london.data.remote.model.details.movie.model.moviecast.MovieCastResponse
import com.london.data.remote.model.details.tvshow.model.TvShowCastRemoteResponse
import com.london.data.remote.model.home.trending.TrendingResponse

interface ActorRemoteDataSource {
    suspend fun getActorDetailsById(id: Int): Result<ActorDetailsResponse>
    suspend fun getActorImagePathById(id: Int): Result<ActorImageResponse>
    suspend fun getTrendingActors(page: Int): Result<ApiResponse<TrendingResponse>>
    suspend fun getMovieActors(movieId: Int): Result<MovieCastResponse>
    suspend fun getTvShowActors(tvShowId: Int): Result<TvShowCastRemoteResponse>
}