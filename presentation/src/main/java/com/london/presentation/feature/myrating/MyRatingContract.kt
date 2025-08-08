package com.london.presentation.feature.myrating

interface MyRatingContract {
    fun onBackClicked()
    fun onMovieClick(id: Int)
    fun onTvShowClick(id: Int)
    fun onDelete(id: Int)
}

fun defaultMyRatingContract() = object : MyRatingContract {
    override fun onBackClicked() {}
    override fun onMovieClick(id: Int) {}
    override fun onTvShowClick(id: Int) {}
    override fun onDelete(id: Int) {}
}
