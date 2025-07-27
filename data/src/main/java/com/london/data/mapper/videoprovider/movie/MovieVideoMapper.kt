package com.london.data.mapper.videoprovider.movie

import com.london.data.remote.model.details.videoprovider.movie.model.MovieVideoRemoteResponse
import com.london.data.utils.asYoutubeUrlOrEmpty
import com.london.data.utils.isTrue
import com.london.domain.entity.videoprovider.MovieVideo

fun MovieVideoRemoteResponse.toMovie(): MovieVideo {
    return MovieVideo(
        id = id.orEmpty(),
        videoUrl = key.asYoutubeUrlOrEmpty(),
        name = name.orEmpty(),
        official = official.isTrue,
        site = site.orEmpty(),
    )
}
