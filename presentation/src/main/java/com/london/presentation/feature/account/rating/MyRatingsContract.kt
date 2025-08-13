package com.london.presentation.feature.account.rating

interface MyRatingsContract {
    fun onBackClicked()
    fun onItemClick(id: Int)
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onRatingCategorySelected(category: RatingCategory)
    fun onDeleteMovie(id: Int)
    fun onDeleteShow(id: Int)
}

fun defaultMyRatingContract() = object : MyRatingsContract {
    override fun onBackClicked() {}
    override fun onItemClick(id: Int) {}
    override fun onDeleteShow(id: Int) {}
    override fun onMovieClick(id: Int) {}
    override fun onTvShowClick(id: Int) {}
    override fun onDeleteMovie(id: Int) {}
    override fun onRatingCategorySelected(category: RatingCategory) {}
}
