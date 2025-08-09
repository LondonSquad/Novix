package com.london.presentation.feature.accountinfo.rating

interface MyRatingContract {
    fun onBackClicked()
    fun onDelete(id: Int)
    fun onItemClick(id: Int)
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onRatingCategorySelected(category: RatingCategory)
}

fun defaultMyRatingContract() = object : MyRatingContract {
    override fun onBackClicked() {}
    override fun onDelete(id: Int) {}
    override fun onItemClick(id: Int) {}
    override fun onMovieClick(id: Int) {}
    override fun onTvShowClick(id: Int) {}
    override fun onRatingCategorySelected(category: RatingCategory) {}
}
