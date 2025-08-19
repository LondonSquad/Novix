package com.london.data.mapper.details

import com.london.data.remote.model.details.ImagesResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.shared.ImagesEntity

fun ImagesResponse.toEntity() = ImagesEntity(
    backdropsUrl = backdrops.orEmpty().map { it.filePath.asImageUrlOrEmpty() },
    id = id.orZero(),
    logosUrl = logos.orEmpty().map { it.filePath.asImageUrlOrEmpty() },
    postersUrl = posters.orEmpty().map { it.filePath.asImageUrlOrEmpty() }
)