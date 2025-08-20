package com.london.domain.repository

import com.london.domain.entity.actor.Actor
import com.london.domain.entity.actor.ActorDetails
import com.london.domain.entity.actor.ActorImageDetails
import com.london.domain.entity.shared.PagedFetchResponse
import com.london.domain.entity.tvshow.cast.TvShowCast

interface ActorRepository {

    suspend fun getActorDetailsById(id: Int): ActorDetails

    suspend fun getActorImagesById(id: Int): ActorImageDetails

    suspend fun getMovieActors(id: Int): List<Actor>

    suspend fun getTrendingActors(page: Int): PagedFetchResponse<Actor>

    suspend fun getTvShowActors(id: Int): TvShowCast
}
