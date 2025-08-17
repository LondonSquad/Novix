package com.london.domain.usecase.details.tvshow

import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodeByIdEntity
import com.london.domain.entity.tvshowdetails.episode.TvShowEpisodesEntity
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetTvEpisodesUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository,
) {
    suspend fun getEpisodeByTvShowId(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): TvShowEpisodeByIdEntity =
        tvShowRepository.getTvShowEpisodeByPosition(tvShowId, seasonNumber, episodeNumber)

    suspend fun getTvShowEpisodesBySeason(tvShowId: Int, seasonNumber: Int): TvShowEpisodesEntity =
        tvShowRepository.getTvShowEpisodesBySeason(tvShowId, seasonNumber)

    suspend fun getEpisodeVideos(
        seriesId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<String> =
        tvShowRepository.getEpisodeVideos(seriesId, seasonNumber, episodeNumber)
}
