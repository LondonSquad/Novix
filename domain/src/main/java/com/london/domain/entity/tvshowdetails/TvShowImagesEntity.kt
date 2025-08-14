package com.london.domain.entity.tvshowdetails

data class TvShowImagesEntity(
    val backdropsUrl: List<String>,
    val id: Int,
    val logosUrl: List<String>,
    val postersUrl: List<String>
)