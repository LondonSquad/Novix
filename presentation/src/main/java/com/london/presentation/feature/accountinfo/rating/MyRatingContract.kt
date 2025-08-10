package com.london.presentation.feature.accountinfo.rating

import com.london.presentation.feature.reviews.MediaType

interface MyRatingContract {
    fun onBackClicked()
    fun onItemClick(id: Int)
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onDelete(id: Int , mediaType: MediaType)
    fun onRatingCategorySelected(category: RatingCategory)
}

fun defaultMyRatingContract() = object : MyRatingContract {
    override fun onBackClicked() {}
    override fun onItemClick(id: Int) {}
    override fun onMovieClick(id: Int) {}
    override fun onTvShowClick(id: Int) {}
    override fun onDelete(id: Int, mediaType: MediaType){}
    override fun onRatingCategorySelected(category: RatingCategory) {}
}
