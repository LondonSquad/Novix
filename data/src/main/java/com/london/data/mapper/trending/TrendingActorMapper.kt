package com.london.data.mapper.trending

import com.london.data.remote.model.trending.TrendingResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.Actor

fun TrendingResponse.toTrendingActor(): Actor = Actor(
    id = id.orZero(),
    name = name.orEmpty(),
    profilePicture = (posterPath ?: profilePath).asImageUrlOrEmpty(),
)