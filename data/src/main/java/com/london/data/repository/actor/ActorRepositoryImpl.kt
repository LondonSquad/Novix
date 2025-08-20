package com.london.data.repository.actor

import com.london.data.mapper.details.actor.toEntity
import com.london.data.mapper.details.movie.toEntity
import com.london.data.mapper.details.tvshow.toCastEntity
import com.london.data.mapper.home.trending.toEntityActor
import com.london.data.remote.source.actor.ActorRemoteDataSource
import com.london.domain.entity.actor.Actor
import com.london.domain.entity.actor.ActorDetails
import com.london.domain.entity.actor.ActorImageDetails
import com.london.domain.entity.shared.PagedFetchResponse
import com.london.domain.entity.tvshow.cast.TvShowCast
import com.london.domain.repository.ActorRepository
import javax.inject.Inject

class ActorRepositoryImpl @Inject constructor(
    private val dataSource: ActorRemoteDataSource
) : ActorRepository {

    override suspend fun getActorDetailsById(id: Int): ActorDetails =
        dataSource.getActorDetailsById(id).getOrThrow().toEntity()

    override suspend fun getActorImagesById(id: Int): ActorImageDetails =
        dataSource.getActorImagePathById(id).getOrThrow().toEntity()

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

    override suspend fun getTvShowActors(id: Int): TvShowCast =
        dataSource.getTvShowActors(id).getOrThrow().toCastEntity()

}
