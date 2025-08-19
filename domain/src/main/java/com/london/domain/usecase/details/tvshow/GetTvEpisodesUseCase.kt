package com.london.domain.usecase.details.tvshow

import com.london.domain.entity.tvshow.episode.EpisodeDetails
import com.london.domain.entity.tvshow.episode.SeasonEpisodes
import com.london.domain.repository.TvShowRepository
import javax.inject.Inject

class GetTvEpisodesUseCase @Inject constructor(
    private val tvShowRepository: TvShowRepository
) {
    suspend fun getEpisodeByTvShowId(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): EpisodeDetails =
        tvShowRepository.getTvShowEpisodeByPosition(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber
        )

    suspend fun getTvShowSeasonEpisodes(
        tvShowId: Int,
        seasonNumber: Int
    ): SeasonEpisodes =
        tvShowRepository.getTvShowSeasonEpisodes(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber
        )

    suspend fun getEpisodeVideos(
        tvShowId: Int,
        seasonNumber: Int,
        episodeNumber: Int
    ): List<String> =
        tvShowRepository.getEpisodeVideos(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber
        )
}
