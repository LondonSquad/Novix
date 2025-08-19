package com.london.domain.usecase.details.actor

import com.london.domain.entity.actor.Actor
import com.london.domain.entity.actor.ActorDetails
import com.london.domain.entity.actor.cast.ActorMediaDetails
import com.london.domain.entity.shared.PagedFetchResponse
import com.london.domain.repository.ActorRepository
import com.london.domain.repository.MovieRepository
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetActorUseCase @Inject constructor(
    private val actorRepository: ActorRepository,
    private val movieRepository: MovieRepository,
    private val tvShowRepository: TvShowRepository,
) {
    suspend fun getActorDetailsById(id: Int) : ActorDetails = actorRepository.getActorDetailsById(id)

    suspend fun getActorImagesById(id: Int): List<String> =
        actorRepository.getActorImagesById(id).imageUrl

    suspend fun getTrendingActors(page: Int): PagedFetchResponse<Actor> =
        actorRepository.getTrendingActors(page)

    suspend fun getActorTvShowPicksById(id: Int) : ActorMediaDetails =
        tvShowRepository.getActorTvShowPicksById(id)

    suspend fun getActorMoviePicksById(id: Int) : ActorMediaDetails =
        movieRepository.getActorMoviePicksById(id)
}
