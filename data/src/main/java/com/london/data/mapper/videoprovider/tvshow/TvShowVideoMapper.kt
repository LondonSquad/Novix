package com.london.data.mapper.videoprovider.tvshow

import com.london.data.datasource.remote.details.videoprovider.tvshow.model.TvShowVideoRemote
import com.london.data.utils.asYoutubeUrlOrEmpty
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.domain.entity.videoprovider.TvShowVideo

fun TvShowVideoRemote.toTvShowVideo(): TvShowVideo {
    return TvShowVideo(
        id = id.orEmpty(),
        iso31661 = iso31661.orEmpty(),
        iso6391 = iso31661.orEmpty(),
        videoUrl = key.asYoutubeUrlOrEmpty(),
        name = name.orEmpty(),
        official = official.isTrue,
        publishedAt = publishedAt.orEmpty(),
        site = site.orEmpty(),
        size = size.orZero(),
        type =type.orEmpty()
    )
}

fun TvShowVideo.TvShowVideoRemote(): TvShowVideoRemote {
    return TvShowVideoRemote(
        id = id,
        iso31661 = iso6391,
        iso6391 = iso6391,
        key = videoUrl,
        name = name,
        official = official,
        publishedAt = publishedAt,
        site = site,
        size = size,
        type = type
    )
}