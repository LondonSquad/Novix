package com.london.data.mapper.trending

import com.london.data.datasource.remote.home.trending.model.TrendingDto
import com.london.domain.entity.trending.Trending
import com.london.data.utils.orZero
import com.london.data.utils.asImageUrlOrEmpty
import com.london.domain.entity.Actor

fun TrendingDto.toTrending(): Trending = Trending(
    id = id.orZero(),
    posterPath = image.asImageUrlOrEmpty(),
    genreIds = genreIds ?: emptyList()
)

fun TrendingDto.toActor(): Actor = Actor(
    id = id,
    name = name ?: title ?: "",
    profilePicture = image.asImageUrlOrEmpty(),
    characterName = ""
) 