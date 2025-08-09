package com.london.data.mapper.details.movie

import com.london.data.remote.model.details.movie.model.movieimages.MovieImagesResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.moviedatails.MovieImages

fun MovieImagesResponse.toEntity() = MovieImages(
    backdrops = backdrops.orEmpty().map { it.filePath.asImageUrlOrEmpty() },
    id = id.orZero(),
    logos = logos.orEmpty().map { it.filePath.asImageUrlOrEmpty() },
    posters = posters.orEmpty().map { it.filePath.asImageUrlOrEmpty() }
)