package com.london.domain.entity

data class ImagesEntity(
    val backdropsUrl: List<String>,
    val id: Int,
    val logosUrl: List<String>,
    val postersUrl: List<String>
)