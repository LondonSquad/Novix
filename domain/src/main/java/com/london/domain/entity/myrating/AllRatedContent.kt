package com.london.domain.entity.myrating

data class AllRatedContent(
    val movies: List<RatedMovie>,
    val tvShows: List<RatedTvShow>
) 