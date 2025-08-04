package com.london.data.mapper.home.trending

import com.london.data.remote.model.home.trending.TrendingResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.orZero
import com.london.domain.entity.Actor

fun TrendingResponse.toEntityActor(): Actor = Actor(
    id = id.orZero(),
    name = name.orEmpty(),
    profilePictureUrl = (posterPath ?: profilePath).asImageUrlOrEmpty(),
    characterName = title.orEmpty()
)
