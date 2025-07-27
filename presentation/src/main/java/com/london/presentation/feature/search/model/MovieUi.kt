package com.london.presentation.feature.search.model

data class MovieUi(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val isSaved: Boolean
)