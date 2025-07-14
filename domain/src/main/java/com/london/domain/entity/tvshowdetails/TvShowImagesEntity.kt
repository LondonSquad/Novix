package com.london.domain.entity.tvshowdetails

data class TvShowImagesEntity(
    val backdrops: List<ImageItemEntity>,
    val id: Int,
    val logos: List<ImageItemEntity>,
    val posters: List<ImageItemEntity>
)