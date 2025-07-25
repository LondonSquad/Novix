package com.london.data.mapper.trending

import com.london.data.remote.model.trending.TrendingResponse
import com.london.domain.entity.trending.Trending
import com.london.data.utils.orZero
import com.london.data.utils.asImageUrlOrEmpty
import com.london.domain.entity.Actor

fun TrendingResponse.toTrending(): Trending = Trending(
    id = id.orZero(),
    posterPath = image.asImageUrlOrEmpty(),
    genreIds = genreIds ?: emptyList()
)

fun TrendingResponse.toActor(): Actor = Actor(
    id = id,
    name = name ?: title ?: "",
    profilePicture = image.asImageUrlOrEmpty(),
    characterName = ""
) 