package com.london.data.repository.actor

import com.london.data.mapper.details.actor.toEntity
import com.london.data.mapper.details.movie.toEntity
import com.london.data.mapper.details.tvshow.toCastEntity
import com.london.data.mapper.home.trending.toEntityActor
import com.london.data.remote.source.actor.ActorRemoteDataSource
import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.entity.actordetails.ActorImageDetails
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.repository.ActorRepository
import javax.inject.Inject

class ActorRepositoryImpl @Inject constructor(
    private val dataSource: ActorRemoteDataSource
) : ActorRepository {

    override suspend fun getActorDetailsById(id: Int): ActorDetails {
        val remoteActorDetails = dataSource.getActorDetailsById(id).getOrThrow()
        return remoteActorDetails.toEntity()
    }

    override suspend fun getActorImagesById(id: Int): ActorImageDetails {
        val remoteActorImages = dataSource.getActorImagePathById(id).getOrThrow()
        return remoteActorImages.toEntity()
    }

    override suspend fun getMovieActors(id: Int): List<Actor> {
        val remoteMovieCast = dataSource.getMovieActors(id).getOrThrow()
        return remoteMovieCast.actorRemote?.map { it.toEntity() }.orEmpty()
    }

    override suspend fun getTrendingActors(page: Int): PagedFetchResponse<Actor> {
        val remoteTrendingActorsResponse = dataSource.getTrendingActors(page).getOrThrow()
        return PagedFetchResponse(
            currentPage = remoteTrendingActorsResponse.currentPage,
            items = remoteTrendingActorsResponse.items.map { it.toEntityActor() },
            totalPages = remoteTrendingActorsResponse.totalPages,
            totalItems = remoteTrendingActorsResponse.totalItems
        )
    }

    override suspend fun getTvShowActors(id: Int): TvShowCastEntity {
        val remoteTvShowCast = dataSource.getTvShowActors(id).getOrThrow()
        return remoteTvShowCast.toCastEntity()
    }
}
