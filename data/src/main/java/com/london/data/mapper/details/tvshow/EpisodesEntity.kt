package com.london.data.mapper.details.tvshow

import com.london.data.remote.model.details.tvshow.model.tvshowepisode.Episode
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.EpisodeBySeasonResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.EpisodeDetailsResponse
import com.london.data.remote.model.details.tvshow.model.tvshowepisode.EpisodeGuestStar
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.data.utils.roundToDecimal
import com.london.domain.entity.Actor
import com.london.domain.entity.tvshowdetails.episode.EpisodeBySeason
import com.london.domain.entity.tvshowdetails.episode.EpisodeDetails
import com.london.domain.entity.tvshowdetails.episode.Episodes

fun EpisodeBySeasonResponse.toEpisodesEntity(): EpisodeBySeason =
    EpisodeBySeason(
        id = seasonId.orEmpty(),
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

