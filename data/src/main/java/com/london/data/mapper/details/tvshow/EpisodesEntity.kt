package com.london.data.mapper.details.tvshow

import com.london.data.remote.model.details.tvshow.episode.Episode
import com.london.data.remote.model.details.tvshow.episode.EpisodeDetailsResponse
import com.london.data.remote.model.details.tvshow.episode.EpisodeGuestStar
import com.london.data.remote.model.details.tvshow.episode.SeasonEpisodesResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.entity.actor.Actor
import com.london.domain.entity.tvshow.episode.EpisodeDetails
import com.london.domain.entity.tvshow.episode.Episodes
import com.london.domain.entity.tvshow.episode.SeasonEpisodes

fun SeasonEpisodesResponse.toEpisodesEntity(): SeasonEpisodes =
    SeasonEpisodes(
        seasonId = seasonId.orEmpty(),
        episodes = episodes.orEmpty().map { it.toEpisodeBySeasonEntity() }
    )

fun Episode.toEpisodeBySeasonEntity(): Episodes =
    Episodes(
        airDate = airDate,
        episodeNumber = episodeNumber.orZero(),
        episodeType = episodeType.orEmpty(),
        id = id.orZero(),
        name = name.orEmpty(),
        overview = overview.orEmpty(),
        runtime = runtime,
        seasonNumber = seasonNumber.orZero(),
        showId = showId.orZero(),
        imageUrl = stillPath.asImageUrlOrEmpty(),
        voteAverage = voteAverage.orZero().roundToDecimal(),
    )

fun EpisodeDetailsResponse.toEpisodeEntity(): EpisodeDetails =
    EpisodeDetails(
        airDate = airDate.orEmpty(),
        seasonNumber = seasonNumber.orZero(),
        tvShowId = id.orZero(),
        name = name.orEmpty(),
        overview = overview.orEmpty(),
        voteAverage = voteAverage.orZero().roundToDecimal(),
        voteCount = voteCount.orZero(),
        guestStars = guestStars.orEmpty().map { it.toActorEntity() },
        id = id.orZero()
    )

fun EpisodeGuestStar.toActorEntity(): Actor =
    Actor(
        id = id.orZero(),
        name = name.orEmpty(),
        profilePictureUrl = profilePath.asImageUrlOrEmpty(),
        characterName = character.orEmpty()
    )

