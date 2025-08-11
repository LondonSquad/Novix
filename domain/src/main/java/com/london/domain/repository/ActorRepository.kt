package com.london.domain.repository

import com.london.domain.entity.Actor
import com.london.domain.entity.PagedFetchResponse
import com.london.domain.entity.actordetails.ActorDetails
import com.london.domain.entity.actordetails.ActorImageDetails
import com.london.domain.entity.tvshowdetails.TvShowCastEntity

interface ActorRepository {
    suspend fun getActorDetailsById(id: Int): ActorDetails
    suspend fun getActorImagesById(id: Int): ActorImageDetails
    suspend fun getMovieCastById(id: Int): List<Actor>
    suspend fun getTrendingActors(page: Int): PagedFetchResponse<Actor>
    suspend fun getCastTvShowById(id: Int): TvShowCastEntity

}