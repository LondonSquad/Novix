package com.london.presentation.feature.myrating

import com.london.presentation.feature.reviews.MediaType

interface MyRatingContract {
    fun onBackClicked()
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onDelete(id: Int , mediaType: MediaType)
    fun onRatingCategorySelected(category: RatingCategory)
    fun onItemClick(id: Int)
}

fun defaultMyRatingContract() = object : MyRatingContract {
    override fun onBackClicked() {}
    override fun onMovieClick(id: Int) {}
    override fun onTvShowClick(id: Int) {}
    override fun onDelete(id: Int, mediaType: MediaType){}
    override fun onRatingCategorySelected(category: RatingCategory) {}
    override fun onItemClick(id: Int) {}
}
