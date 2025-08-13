package com.london.domain.usecase.details.tvshow

import com.london.domain.repository.ActorRepository
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetTvEpisodesUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository,
    private val actorRepository: ActorRepository
) {
    suspend fun getEpisodeByTvShowId(tvShowId: Int, seasonNumber: Int, episodeNumber: Int) =
        tvShowRepository.getTvShowEpisodeByPosition(tvShowId, seasonNumber, episodeNumber)

    suspend fun getCastById(tvShowId: Int) = actorRepository.getCastTvShowById(tvShowId)

    suspend fun getTvShowEpisodesBySeason(tvShowId: Int, seasonNumber: Int) =
        tvShowRepository.getTvShowEpisodesBySeason(tvShowId, seasonNumber)

    suspend fun getEpisodeVideos(seriesId: Int, seasonNumber: Int, episodeNumber: Int) =
        tvShowRepository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
}
