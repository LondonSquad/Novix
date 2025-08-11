package com.london.domain.usecase.details.tvshow

import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class ManageTvEpisodesUseCase @Inject constructor(
    private val repository: TvShowRepository
) {
    suspend fun getEpisodeByTvShowId(tvShowId: Int, seasonNumber: Int, episodeNumber: Int) =
        repository.getTvShowEpisodeByPosition(tvShowId, seasonNumber, episodeNumber)

    suspend fun getCastById(tvShowId: Int) = repository.getCastTvShowById(tvShowId)

    suspend fun getTvShowEpisodesBySeason(tvShowId: Int, seasonNumber: Int) =
        repository.getTvShowEpisodesBySeason(tvShowId, seasonNumber)

    suspend fun getEpisodeVideos(seriesId: Int, seasonNumber: Int, episodeNumber: Int) =
        repository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)

}