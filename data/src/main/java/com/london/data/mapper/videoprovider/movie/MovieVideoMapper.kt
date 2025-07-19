package com.london.data.mapper.videoprovider.movie

import com.london.data.datasource.remote.details.videoprovider.movie.model.MovieVideoRemote
import com.london.data.utils.asYoutubeUrlOrEmpty
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.domain.entity.videoprovider.MovieVideo

fun MovieVideoRemote.toMovie(): MovieVideo {
    return MovieVideo(
        id = id.orEmpty(),
        iso31661 = iso31661.orEmpty(),
        iso6391 = iso6391.orEmpty(),
        videoUrl = key.asYoutubeUrlOrEmpty(),
        name = name.orEmpty(),
        official = official.isTrue,
        publishedAt = publishedAt.orEmpty(),
        site = site.orEmpty(),
        size = size.orZero(),
        type = type.orEmpty()
    )
}

fun MovieVideo.toMovieRemote(): MovieVideoRemote {
    return MovieVideoRemote(
        id = id,
        iso31661 = iso31661,
        iso6391 = iso6391,
        key = videoUrl,
        name = name,
        official = official,
        publishedAt = publishedAt,
        site = site,
        size = size,
        type = type,
    )
}
