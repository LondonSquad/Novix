package com.london.presentation.feature.details.movie

interface MovieDetailsContract {
    fun onRetry()
    fun onBackClick()
    fun onLoginClick()
    fun onSavedClick()
    fun onExpandClick()
    fun onRateBottomSheetClick()
    fun onMovieClick(movieId: Int)
    fun onActorClick(actorId: Int)
    fun onGenreClick(genreId: Int)
    fun onSelectRatingClick(rating: Int)
    fun onReviewsClick(movieId: Int, mediaNumber: Int)
}