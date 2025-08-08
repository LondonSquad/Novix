package com.london.presentation.feature.myrating

import com.london.domain.entity.myrating.MediaItem
import com.london.domain.entity.myrating.RatedMovie
import com.london.domain.entity.myrating.RatedTvShow

internal fun RatedMovie.toMediaItem(): MediaItem = MediaItem(
    id = id,
    posterPath = posterPath,
    title = title,
    rating = rating,
    isMovie = true
)

internal fun RatedTvShow.toMediaItem(): MediaItem = MediaItem(
    id = id,
    posterPath = posterPath,
    title = title,
    rating = rating,
    isMovie = false
) 