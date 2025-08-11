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
    override suspend fun getActorDetailsById(id: Int): ActorDetails =
        dataSource.getActorDetailsById(id).getOrThrow().toEntity()

    override suspend fun getActorImagesById(id: Int): ActorImageDetails =
        dataSource.getActorImagePathById(id).getOrThrow().toEntity()

    override suspend fun getMovieActors(id: Int): List<Actor> {
        val movieCast = dataSource.getMovieActors(id).getOrThrow()
        return movieCast.actorRemote?.map { it.toEntity() }.orEmpty()
    }

    override suspend fun getTrendingActors(page: Int): PagedFetchResponse<Actor> {
        val response = dataSource.getTrendingActors(page).getOrThrow()
        return PagedFetchResponse(
            currentPage = response.currentPage,
            items = response.items.map { it.toEntityActor() },
            totalPages = response.totalPages,
            totalItems = response.totalItems
        )
    }

    override suspend fun getCastTvShowById(id: Int): TvShowCastEntity =
        dataSource.getTvShowActors(id).getOrThrow().toCastEntity()
}
