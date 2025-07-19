package com.london.presentation.features.search.model

data class MovieUi(
    val id: Int,
    val title: String,
    val posterUrl: String,
    val isSaved: Boolean
)